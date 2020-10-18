package co.uk.redpixel.wardrobe.config.syntax

import co.uk.redpixel.wardrobe.domain.Port
import pureconfig.ConfigReader

trait ReaderImplicits {

  implicit val PortReader: ConfigReader[Port] =
    ConfigReader[Int].map(Port)
}
