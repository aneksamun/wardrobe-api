package co.uk.redpixel.wardrobe.domain.persistence

import co.uk.redpixel.wardrobe.domain.model.Category

trait CategoryRepository[F[_]] {

  def add(category: Category): F[Unit]
}
