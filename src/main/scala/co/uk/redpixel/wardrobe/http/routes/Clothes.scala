package co.uk.redpixel.wardrobe.http.routes

import cats.effect.Sync
import co.uk.redpixel.wardrobe.http.search.{Limit, Offset}
//import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.{HttpRoutes, QueryParamDecoder}

object Clothes {

  def routes[F[_] : Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "api" / "clothes" / name =>
        for {
          resp <- Ok(name)
        } yield resp
      case GET -> Root / "api" / "clothes" :? OffsetQueryParam(offset) +& LimitQueryParam(limit) =>
        for {
          resp <- Ok("OK")
        } yield resp
      case r@PUT -> Root / "api" / "clothes" / name / "outfit" =>
        for {
          resp <- Ok("TAG")
        } yield resp
      case r@POST -> Root / "api" / "clothes" =>
        for {
          resp <- Ok("TAG")
        } yield resp
    }
  }

  implicit val offsetQueryParamDecoder: QueryParamDecoder[Offset] =
    QueryParamDecoder[Int].map(Offset(_))

  implicit val limitQueryParamDecoder: QueryParamDecoder[Limit] =
    QueryParamDecoder[Int].map(Limit(_))


  object OffsetQueryParam extends QueryParamDecoderMatcher[Offset]("offset")

  object LimitQueryParam extends QueryParamDecoderMatcher[Limit]("limit")

}
