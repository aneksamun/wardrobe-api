package co.uk.redpixel.wardrobe.domain.persistence

import co.uk.redpixel.wardrobe.domain.model.Outfit

trait OutfitRepository[F[_]] {

  def add(outfit: Outfit): F[Unit]
}
