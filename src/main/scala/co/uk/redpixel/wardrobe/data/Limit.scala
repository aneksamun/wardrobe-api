package co.uk.redpixel.wardrobe.data

final case class Limit(value: Int) extends AnyVal

object Limit {
  implicit def limit2int(limit: Limit): Int = limit.value
}
