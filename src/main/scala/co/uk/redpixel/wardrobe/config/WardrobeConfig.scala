package co.uk.redpixel.wardrobe.config

import cats.Applicative
import cats.data.EitherT
import com.typesafe.config.ConfigFactory
import pureconfig.error.ConfigReaderFailures
import pureconfig.syntax.ConfigReaderOps

final case class WardrobeConfig(server: ServerConfig, db: DatabaseConfig)

object WardrobeConfig {
  import pureconfig.generic.auto._
  import eu.timepit.refined.pureconfig._

  def load[F[_]](implicit F: Applicative[F]): EitherT[F, ConfigReaderFailures, WardrobeConfig] = EitherT {
    F.pure(ConfigFactory.load.to[WardrobeConfig])
  }
}
