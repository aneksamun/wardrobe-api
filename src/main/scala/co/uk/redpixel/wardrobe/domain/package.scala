package co.uk.redpixel.wardrobe

package object domain {

  final case class UserName(value: String) extends AnyVal
  final case class Password(value: String) extends AnyVal
}
