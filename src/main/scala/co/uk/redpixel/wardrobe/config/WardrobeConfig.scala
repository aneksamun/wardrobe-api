package co.uk.redpixel.wardrobe.config

import com.typesafe.config.ConfigFactory
import pureconfig.ConfigReader.Result
import pureconfig.syntax.ConfigReaderOps

final case class WardrobeConfig(server: ServerConfig, db: DatabaseConfig)

object WardrobeConfig {
  import implicits._
  import pureconfig.generic.auto._

  def load(): Result[WardrobeConfig] = ConfigFactory.load.to[WardrobeConfig]
}
