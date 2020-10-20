package co.uk.redpixel.wardrobe

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import co.uk.redpixel.wardrobe.config.WardrobeConfig
import co.uk.redpixel.wardrobe.http.routes.{Clothes, HealthCheck}
import co.uk.redpixel.wardrobe.infrastructure.persistence._
import co.uk.redpixel.wardrobe.persistence.Database
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import pureconfig.error.ConfigReaderFailures

import scala.concurrent.ExecutionContext.global

object WardrobeApiServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      config <- Stream.eval(WardrobeConfig.load[F]().valueOr(terminate()))

      xa = Database.connect[F](config.db)
      _ <- Stream.eval(Database.createSchema[F](config.db)(xa))

//      clothingAlg = ClothingAlg.impl[F](xa)// ClothesSupply(), CategorySupply(), OutfitSupply())

      httpApp = (
        Clothes.routes[F]() <+>
        HealthCheck.routes[F]()
      ).orNotFound

      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(config.server.httpPort, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain


  private def terminate(): ConfigReaderFailures => WardrobeConfig = { errors =>
    throw new IllegalStateException(s"Could not load AppConfig: ${errors.toList.mkString("\n")}")
  }
}
