package co.uk.redpixel.wardrobe

sealed abstract class WardrobeError(message: String)
  extends Exception(message)

final case class OutfitTagError(message: String)
  extends WardrobeError(message)
