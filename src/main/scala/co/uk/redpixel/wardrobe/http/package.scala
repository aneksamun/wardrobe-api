package co.uk.redpixel.wardrobe

import cats.data.ValidatedNel
import cats.syntax.all._
import co.uk.redpixel.wardrobe.data.search.{Limit, Offset}
import eu.timepit.refined.numeric._
import eu.timepit.refined.refineV
import eu.timepit.refined.types.all
import eu.timepit.refined.types.all.PosInt
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParameterValue}

package object http {

  implicit object PosIntQueryParamDecoder extends QueryParamDecoder[PosInt] {

    def decode(value: QueryParameterValue): ValidatedNel[ParseFailure, all.PosInt] =
      QueryParamDecoder[Int].decode(value).andThen { param =>
        refineV[Positive](param)
          .leftMap(s => ParseFailure("Must to be a positive number", s))
          .toValidatedNel
      }
  }

  implicit object LimitParamDecoder extends QueryParamDecoder[Limit] {
    def decode(value: QueryParameterValue): ValidatedNel[ParseFailure, Limit] =
      QueryParamDecoder[Int].decode(value).map(Limit(_))
  }

  implicit object OffsetParamDecoder extends QueryParamDecoder[Offset] {
    def decode(value: QueryParameterValue): ValidatedNel[ParseFailure, Offset] =
      QueryParamDecoder[Int].decode(value).map(Offset(_))
  }
}
