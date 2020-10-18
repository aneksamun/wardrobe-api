package co.uk.redpixel.wardrobe.domain.model

import co.uk.redpixel.wardrobe.domain.ClothesId

final case class Clothes(id: ClothesId,
                         name: String,
                         category: Option[Category] = None,
                         outfit: Option[Outfit] = None)
