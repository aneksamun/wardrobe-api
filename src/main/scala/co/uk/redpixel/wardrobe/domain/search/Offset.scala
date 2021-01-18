package co.uk.redpixel.wardrobe.data

final case class Offset(value: Int) extends AnyVal

object Offset {
  implicit def offsetToInt(offset: Offset): Int = offset.value
}
