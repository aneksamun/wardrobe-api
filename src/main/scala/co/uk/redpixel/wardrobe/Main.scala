package co.uk.redpixel.wardrobe

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    WardrobeApiServer.stream[IO]
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
