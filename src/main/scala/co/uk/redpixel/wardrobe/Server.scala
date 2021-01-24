package co.uk.redpixel.wardrobe

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.syntax.all._
import co.uk.redpixel.wardrobe.config.WardrobeConfig
import co.uk.redpixel.wardrobe.http.route.{Clothes, HealthCheck}
import co.uk.redpixel.wardrobe.persistence.{Database, DoobieClothesStore}
import eu.timepit.refined.auto._
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import pureconfig.error.ConfigReaderFailures

import scala.concurrent.ExecutionContext.global

object WardrobeApiServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      // configuration
      config <- Stream.eval(WardrobeConfig.load[F].valueOr(terminate()))

      // database
      xa <- Stream.resource(Database.connect[F](config.db))
      _  <- Stream.eval(Database.createSchema[F](config.db)(xa))

      clothesStore = DoobieClothesStore[F](xa)

      // routes
      routes = (
        Clothes.routes[F](clothesStore) <+>
        HealthCheck.routes[F](clothesStore)
      ).orNotFound

      // request logging
      httpApp = Logger.httpApp(logHeaders = true, logBody = true)(routes)

      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(config.server.httpPort, "0.0.0.0")
        .withHttpApp(httpApp)
        .serve
    } yield exitCode
  }.drain


  private def terminate(): ConfigReaderFailures => WardrobeConfig = { errors =>
    throw new IllegalStateException(s"Configuration read error: ${errors.toList.map(_.description).mkString("\n")}")
  }
}
