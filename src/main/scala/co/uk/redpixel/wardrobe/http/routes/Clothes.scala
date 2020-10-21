package co.uk.redpixel.wardrobe.http.routes

import cats.Monad
import cats.effect.Sync
import cats.implicits.{toTraverseOps, _}
import co.uk.redpixel.wardrobe.data.{Limit, Offset}
import co.uk.redpixel.wardrobe.persistence.services.ClothingAlg
import fs2.text.{lines, utf8Decode}
import org.http4s.EntityDecoder.multipart
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.multipart.Part
import org.http4s.{HttpRoutes, QueryParamDecoder}

object Clothes {

  def routes[F[_] : Sync : Monad](clothes: ClothingAlg[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "api" / "clothes" =>
        req.decodeWith(multipart[F], strict = true) { form =>
          def csvFilePart(part: Part[F]): Boolean =
            part.headers.toList.exists(_.value == "text/csv")

          form.parts.filter(csvFilePart)
            .traverse(_.body.through(utf8Decode).through(lines))
            .compile
            .foldMonoid
            .flatMap(clothes.add)
            .flatMap(stream => Ok("Size of items: " + stream))
        }
      //      case GET -> Root / "api" / "clothes" / name =>
      //        for {
      //          resp <- Ok(name)
      //        } yield resp
      //      case GET -> Root / "api" / "clothes" :? OffsetQueryParam(offset) +& LimitQueryParam(limit) =>
      //        for {
      //          resp <- Ok("OK")
      //        } yield resp
      //      case r@PUT -> Root / "api" / "clothes" / name / "outfit" =>
      //        for {
      //          resp <- Ok("TAG")
      //        } yield resp
    }
  }

  implicit val offsetQueryParamDecoder: QueryParamDecoder[Offset] =
    QueryParamDecoder[Int].map(Offset(_))

  implicit val limitQueryParamDecoder: QueryParamDecoder[Limit] =
    QueryParamDecoder[Int].map(Limit(_))


  object OffsetQueryParam extends QueryParamDecoderMatcher[Offset]("offset")

  object LimitQueryParam extends QueryParamDecoderMatcher[Limit]("limit")

}
