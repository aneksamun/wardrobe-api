package co.uk.redpixel.wardrobe.data

final case class Category(name: String)

final case class Outfit(name: String)

final case class Clothes(name: String,
                         category: Option[Category] = None,
                         outfit: Option[Outfit] = None) {

  def maybeCategoryName(): Option[String] = category.map(_.name)

  def maybeOutfitName(): Option[String] = outfit.map(_.name)
}
