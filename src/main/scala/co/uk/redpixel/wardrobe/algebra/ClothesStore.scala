package co.uk.redpixel.wardrobe.algebra

import cats.data.{EitherT, OptionT}
import co.uk.redpixel.wardrobe.data.{Clothes, Outfit}
import co.uk.redpixel.wardrobe.data.Search.{Limit, Offset}

trait ClothesStore[F[_]] {

  def isAlive: F[Boolean]

  def add(data: Vector[Clothes]): F[Total]

  def find(name: String): OptionT[F, Clothes]

  def tag(name: String, outfit: Outfit): EitherT[F, OutfitTagError, Clothes]

  def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]]

  def countAll: F[Total]
}
