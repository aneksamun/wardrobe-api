package co.uk.redpixel.wardrobe.infrastructure

import co.uk.redpixel.wardrobe.domain.persistence.{CategoryRepository, ClothesRepository, OutfitRepository}
import doobie.ConnectionIO

package object persistence {

  object CategorySupply {
    def apply(): CategoryRepository[ConnectionIO] =
      new DoobieCategoryRepository()
  }

  object ClothesSupply {
    def apply(): ClothesRepository[ConnectionIO] =
      new DoobieClothesRepository()
  }

  object OutfitSupply {
    def apply(): OutfitRepository[ConnectionIO] =
      new DoobieOutfitRepository()
  }
}
