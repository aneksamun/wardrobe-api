package co.uk.redpixel.wardrobe

import cats.data.ValidatedNel
import cats.syntax.all._
import eu.timepit.refined.numeric._
import eu.timepit.refined.refineV
import eu.timepit.refined.types.all
import eu.timepit.refined.types.all.{NonNegInt, PosInt}
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

  implicit object NonNegIntQueryParamDecoder extends QueryParamDecoder[NonNegInt] {

    def decode(value: QueryParameterValue): ValidatedNel[ParseFailure, all.NonNegInt] =
      QueryParamDecoder[Int].decode(value).andThen { param =>
        refineV[NonNegative](param)
          .leftMap(s => ParseFailure("Must to be greater or equal to 0", s))
          .toValidatedNel
      }
  }
}
