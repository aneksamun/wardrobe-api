package co.uk.redpixel.wardrobe.domain

final case class Port(value: Int) {
  assert(value >= 0)
  assert(value <= 65535)
}

object Port {
  implicit def portToInt(port: Port): Int = port.value
}
