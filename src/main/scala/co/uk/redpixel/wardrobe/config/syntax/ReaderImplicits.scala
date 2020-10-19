package co.uk.redpixel.wardrobe.config.syntax

import co.uk.redpixel.wardrobe.domain.{Password, Port, UserName}
import pureconfig.ConfigReader

trait ReaderImplicits {

  implicit val PortReader: ConfigReader[Port] =
    ConfigReader[Int].map(Port(_))

  implicit val UserNameReader: ConfigReader[UserName] =
    ConfigReader[String].map(UserName)

  implicit val PasswordReader: ConfigReader[Password] =
    ConfigReader[String].map(Password)
}
