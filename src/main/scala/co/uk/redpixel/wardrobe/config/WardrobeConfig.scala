package co.uk.redpixel.wardrobe.config

import cats.Applicative
import cats.data.EitherT
import cats.implicits.catsSyntaxApplicativeId
import com.typesafe.config.ConfigFactory
import pureconfig.error.ConfigReaderFailures
import pureconfig.syntax.ConfigReaderOps

final case class WardrobeConfig(server: ServerConfig, db: DatabaseConfig)

object WardrobeConfig {
  import syntax._
  import pureconfig.generic.auto._

  def load[F[_]: Applicative](): EitherT[F, ConfigReaderFailures, WardrobeConfig] = EitherT {
    ConfigFactory.load.to[WardrobeConfig].pure[F]
  }
}
