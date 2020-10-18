package co.uk.redpixel.wardrobe

import pureconfig.ConfigReader

package object config {

  type UserName = String
  type Password = String

  final case class Port(value: Int) {
    assert(value >= 0)
    assert(value <= 65535)
  }

  object implicits {

    implicit val PortReader: ConfigReader[Port] =
      ConfigReader[Int].map(Port)
  }
}
