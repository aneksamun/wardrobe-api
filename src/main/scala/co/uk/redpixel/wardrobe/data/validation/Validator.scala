package co.uk.redpixel.wardrobe.data.validation

trait Validator[T] {

  def validate(target: T): ValidationResult
}
