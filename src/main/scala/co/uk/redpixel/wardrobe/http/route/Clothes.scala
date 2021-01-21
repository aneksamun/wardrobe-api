package co.uk.redpixel.wardrobe.http.route

import cats.Monad
import cats.effect.Sync
import cats.syntax.all._
import co.uk.redpixel.wardrobe.data.Outfit
import co.uk.redpixel.wardrobe.data.Search.{Limit, Offset}
import co.uk.redpixel.wardrobe.data.csv.Report
import co.uk.redpixel.wardrobe.http.PosIntQueryParamDecoder
import co.uk.redpixel.wardrobe.persistence.service.ClothingAlg
import fs2.text.{lines, utf8Decode}
import io.circe.generic.auto._
import org.http4s.EntityDecoder.multipart
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.multipart.Part

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
            .flatMap(total => Ok(Report(total)))
        }

      case GET -> Root / "api" / "clothes" / name if !name.isBlank =>
        clothes.find(name) >>= (_.fold(NoContent())(content => Ok(content)))

      case GET -> Root / "api" / "clothes" :? OffsetQueryParam(offset) +& LimitQueryParam(limit) =>
        clothes.findAll(offset, limit).flatMap(page => Ok(page))

      case req @ PUT -> Root / "api" / "clothes" / name / "outfit" =>
        req.decode[Outfit] { outfit =>
          for {
            maybeTagged <- clothes.tag(name, outfit)
            response <- maybeTagged.fold(NoContent())(content => Ok(content))
          } yield response
        }
    }
  }

  object OffsetQueryParam extends QueryParamDecoderMatcher[Offset]("offset")

  object LimitQueryParam extends QueryParamDecoderMatcher[Limit]("limit")
}
