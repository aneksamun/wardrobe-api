package co.uk.redpixel.wardrobe.data.search

import cats.data.NonEmptyList
import co.uk.redpixel.wardrobe.data.validation.{FieldError, Validator}

final case class Offset(value: Int) extends AnyVal {
  override def toString: String = value.toString
}

object Offset {

  val validator: Validator[Offset] = (offset: Offset) =>
    Option.when(offset.value < 0)(NonEmptyList.of(FieldError("offset", "Must be non negative")))

  implicit def offset2int(offset: Offset): Int =
    offset.value
}
