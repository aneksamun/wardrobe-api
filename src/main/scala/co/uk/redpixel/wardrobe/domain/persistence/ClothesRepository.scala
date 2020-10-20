package co.uk.redpixel.wardrobe.domain.persistence

import co.uk.redpixel.wardrobe.domain.model.Clothes
import co.uk.redpixel.wardrobe.http.Offset
import co.uk.redpixel.wardrobe.http.search.{Limit, Offset}

trait ClothesRepository[F[_]] {

  def upsert(clothes: Clothes): F[Unit]

  def findByName(name: String): F[Option[Clothes]]

  def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]]
}
