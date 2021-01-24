package co.uk.redpixel.wardrobe.data

final case class Clothes(name: String,
                         category: Category,
                         outfit: Option[Outfit] = None)
