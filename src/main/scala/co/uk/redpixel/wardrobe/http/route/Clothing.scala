package co.uk.redpixel.wardrobe.http.route

import cats.Monad
import cats.effect.Sync
import cats.syntax.all._
import co.uk.redpixel.wardrobe.algebra.ClothesStore
import co.uk.redpixel.wardrobe.data.csv.instances._
import co.uk.redpixel.wardrobe.data.csv.syntax._
import co.uk.redpixel.wardrobe.data.search.{Limit, Offset}
import co.uk.redpixel.wardrobe.data.{Clothes, Outfit}
import co.uk.redpixel.wardrobe.http._
import co.uk.redpixel.wardrobe.http.serdes.{Report, SearchPage, _}
import fs2.text.{lines, utf8Decode}
import io.circe.generic.auto._
import org.http4s.EntityDecoder.multipart
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.multipart.Part

object Clothing {

  def routes[F[_] : Sync : Monad](clothesStore: ClothesStore[F]): HttpRoutes[F] = {
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
            .map(_.into[Clothes])
            .flatMap(clothesStore.add)
            .flatMap(total => Ok(Report(total)))
        }

      case GET -> Root / "api" / "clothes" / name if !name.isBlank =>
        clothesStore.find(name).foldF(NotFound())(Ok(_))

      case GET -> Root / "api" / "clothes" :? OffsetQueryParam(offset) +& LimitQueryParam(limit) =>
        Offset.validator.validate(offset) |+| Limit.validator.validate(limit) match {
          case None =>
            Ok {
              for {
                items <- clothesStore.findAll(offset, limit)
                total <- clothesStore.countAll
              } yield SearchPage(items, total)
            }
          case Some(errors) => BadRequest(errors.toList)
        }

      case req @ PUT -> Root / "api" / "clothes" / name / "outfit" =>
        req.decode[Outfit](outfit =>
          clothesStore.tag(name, outfit).foldF(BadRequest(_), Ok(_))
        )
    }
  }

  object OffsetQueryParam extends QueryParamDecoderMatcher[Offset]("offset")

  object LimitQueryParam extends QueryParamDecoderMatcher[Limit]("limit")
}
