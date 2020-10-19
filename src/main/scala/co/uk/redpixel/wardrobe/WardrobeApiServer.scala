package co.uk.redpixel.wardrobe

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import co.uk.redpixel.wardrobe.config.WardrobeConfig
import co.uk.redpixel.wardrobe.infrastructure.persistence.Schema
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object WardrobeApiServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      config <- Stream.eval(WardrobeConfig.load[F]())

//      config <- Stream.eval(WardrobeConfig.load[F]().valueOr { errors =>
//        throw new IllegalStateException(s"Could not load AppConfig: ${errors.toList.mkString("\n")}")
//      })



      // build transactor
      // migrate database

      client <- BlazeClientBuilder[F](global).stream

//      config = WardrobeConfig.load().map(config => Option.when(config.db.createSchema)(Schema.migrate(_)))


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
}
