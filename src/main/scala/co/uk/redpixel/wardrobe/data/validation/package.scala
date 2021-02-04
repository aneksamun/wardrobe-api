package co.uk.redpixel.wardrobe.data

import cats.data.NonEmptyList

package object validation {

  type FieldName = String
  type Message = String

  final case class FieldError(fieldName: FieldName, message: Message)

  type ValidationResult = Option[NonEmptyList[FieldError]]
}
