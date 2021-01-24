package co.uk.redpixel.wardrobe

package object algebra {

  type Total = Int

  sealed abstract class WardrobeError(message: String)
    extends Exception(message)

  final case class OutfitTagError(message: String)
    extends WardrobeError(message)
}
