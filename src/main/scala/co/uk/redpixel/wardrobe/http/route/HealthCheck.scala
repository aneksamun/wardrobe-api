package co.uk.redpixel.wardrobe.http.route

import cats.effect.Sync
import cats.syntax.all._
import co.uk.redpixel.wardrobe.algebra.ClothesStore
import io.circe.Json
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

object HealthCheck {

  def routes[F[_] : Sync](store: ClothesStore[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "internal" / "status" =>
        store.isAlive >>= { status =>
          Ok(Json.obj("healthy" := status))
        }
    }
  }
}
