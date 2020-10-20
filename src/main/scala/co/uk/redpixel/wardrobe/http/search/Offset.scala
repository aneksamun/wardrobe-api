package co.uk.redpixel.wardrobe.http.search

final case class Offset(value: Int) extends AnyVal

object Offset {
  implicit def offset2int(offset: Offset): Int = offset.value
}
