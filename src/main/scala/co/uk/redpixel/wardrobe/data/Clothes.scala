package co.uk.redpixel.wardrobe.data

final case class Clothes(name: String,
                         category: Option[Category] = None,
                         outfit: Option[Outfit] = None)
