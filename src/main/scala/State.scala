import java.nio.charset.StandardCharsets

import cats.Applicative
import com.github.oskin1.scakoo.TaggingStrategy.MurmurHash3Strategy
import com.github.oskin1.scakoo.mutable.CuckooFilter
import com.github.oskin1.scakoo.{Funnel, Sink}
import com.google.common.primitives.{Bytes, Longs}
import io.circe._
import io.circe.syntax._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import scala.collection.concurrent.TrieMap

object State {
  implicit val recFunnel: Funnel[Record] = { rec =>
    Sink(Bytes.concat(rec.eventName.getBytes(StandardCharsets.UTF_8), Longs.toByteArray(rec.value)))
  }

  private val bucketsCount = Math.pow(2, 31.3).toInt
  private val records: TrieMap[String, Int] = TrieMap.empty
  // So hashtable could contain nearly 10^10 entries
  private val processedValuesFilter = CuckooFilter[Record](4, bucketsCount)

  def insertRecord(rec: Record): Unit = {
    def incrementEntryValueCount(eventName: String): Option[Int] =
      records.get(eventName).fold(records.put(eventName, 1))(res => records.put(eventName, res + 1))

    if (!processedValuesFilter.lookup(rec)) {
      processedValuesFilter.insert(rec)
      incrementEntryValueCount(rec.eventName)
    }
  }

  implicit val recordsEncoder: Encoder[TrieMap[String, Int]] = Encoder.encodeMapLike[String, Int, TrieMap]

  implicit def recordsEntityEncoder[F[_] : Applicative]: EntityEncoder[F, Json] =
    jsonEncoderOf

  def getRecordsJson: Json = records.asJson
}
