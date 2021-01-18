package co.uk.redpixel.wardrobe.data

final case class Limit(value: Int) extends AnyVal

object Limit {
  implicit def limitToInt(limit: Limit): Int = limit.value
}
