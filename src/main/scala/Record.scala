import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto._
import org.http4s.EntityDecoder
import org.http4s.circe._

case class Record(eventName: String, value: Long)

object Record {
  implicit val recordDecoder: Decoder[Record] = deriveDecoder[Record]

  implicit def recordEntityDecoder[F[_] : Sync]: EntityDecoder[F, Record] =
    jsonOf
}
