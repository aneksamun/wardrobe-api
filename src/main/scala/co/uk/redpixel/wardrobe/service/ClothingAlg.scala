package co.uk.redpixel.wardrobe.service

import cats.data.{EitherT, OptionT}
import co.uk.redpixel.wardrobe.data.Search.{Limit, Offset}
import co.uk.redpixel.wardrobe.data.{Clothes, Outfit}

trait ClothingAlg[F[_]] {

  type Total = Int

  def add(data: Vector[String]): F[Total]

  def find(name: String): OptionT[F, Clothes]

  def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]]

  def tag(name: String, outfit: Outfit): EitherT[F, OutfitTagError, Clothes]
}
