package co.uk.redpixel.wardrobe.data.search

final case class Limit(value: Int) extends AnyVal

object Limit {
  implicit def limitToInt(limit: Limit): Int = limit.value
}
