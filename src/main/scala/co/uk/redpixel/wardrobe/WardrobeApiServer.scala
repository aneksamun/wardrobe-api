package co.uk.redpixel.wardrobe

import cats.effect.{ConcurrentEffect, ContextShift, Sync, Timer}
import cats.implicits._
import co.uk.redpixel.wardrobe.config.WardrobeConfig
import co.uk.redpixel.wardrobe.infrastructure.persistence.Database
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
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

      client <- BlazeClientBuilder[F](global).stream

      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        WardrobeapiRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        WardrobeapiRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound

      // With Middlewares in place
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
