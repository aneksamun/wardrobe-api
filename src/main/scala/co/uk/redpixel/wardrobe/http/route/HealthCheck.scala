package co.uk.redpixel.wardrobe.http.route

import cats.effect.Sync
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

object HealthCheck {

  def routes[F[_] : Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "internal" / "status" =>
        Ok(StatusResponse(healthy = true))
    }
  }

  final case class StatusResponse(healthy: Boolean)

}
