package co.uk.redpixel.wardrobe.http

import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder, Printer}
import org.http4s.circe.CirceInstances
import org.http4s.{EntityDecoder, EntityEncoder}

package object serdes extends CirceInstances {

  override val defaultPrinter: Printer = Printer.noSpaces.copy(dropNullValues = true)

  implicit def circeEntityDecoder[F[_] : Sync, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
  implicit def circeEntityEncoder[F[_] : Applicative, A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]
}
