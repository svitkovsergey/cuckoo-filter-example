import State._
import cats.effect.implicits._
import cats.effect.{ConcurrentEffect, Sync}
import cats.implicits._
import jawnfs2._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}
import org.log4s.{Logger, getLogger}

object Routes {
  private val logger: Logger = getLogger
  implicit val f = io.circe.jawn.CirceSupportParser.facade

  def recordsRoutes[F[_] : Sync : ConcurrentEffect](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "reports" =>
        Ok(State.getRecordsJson)

      case req@POST -> Root / "data" =>
        req.body.chunks.parseJsonStream.map { j =>
          j.as[Record] match {
            case Right(record) => State.insertRecord(record)
            case Left(decodingFailure) => logger.error(decodingFailure)("Failed to decode incoming value")
          }
        }.compile.drain.toIO.map(_ => Response[F](Ok).pure[F]).unsafeRunSync()
    }
  }
}
