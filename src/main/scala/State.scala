import cats.Applicative
import io.circe._
import io.circe.syntax._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import scala.collection.concurrent.TrieMap

object State {
  private val records: TrieMap[String, Int] = TrieMap.empty
  private val processedValues: TrieMap[String, Set[Long]] = TrieMap.empty

  def insertRecord(rec: Record): Unit = {
    def incrementEntryValueCount(eventName: String): Option[Int] =
      records.get(eventName).fold(records.put(eventName, 1))(res => records.put(eventName, res + 1))

    processedValues.get(rec.eventName) match {
      case Some(vs) =>
        if (!vs.contains(rec.value)) {
          processedValues.put(rec.eventName, vs + rec.value)
          incrementEntryValueCount(rec.eventName)
        }
        ()
      case None =>
        processedValues.put(rec.eventName, Set(rec.value))
        incrementEntryValueCount(rec.eventName)
        ()
    }
  }

  implicit val recordsEncoder: Encoder[TrieMap[String, Int]] = Encoder.encodeMapLike[String, Int, TrieMap]

  implicit def recordsEntityEncoder[F[_] : Applicative]: EntityEncoder[F, Json] =
    jsonEncoderOf

  def getRecordsJson: Json = records.asJson
}
