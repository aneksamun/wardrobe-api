package co.uk.redpixel.wardrobe.data.search

import cats.data.NonEmptyList
import co.uk.redpixel.wardrobe.data.validation.{FieldError, Validator}

final case class Limit(value: Int) extends AnyVal {
  override def toString: String = value.toString
}

object Limit {

  val validator: Validator[Limit] = (limit: Limit) =>
    Option.when(limit.value < 1)(NonEmptyList.of(FieldError("limit", "Must be greater than 0")))

  implicit def limit2int(limit: Limit): Int =
    limit.value
}
