package co.uk.redpixel.wardrobe.domain.model

final case class Clothes(name: String,
                         category: Option[Category] = None,
                         outfit: Option[Outfit] = None)
