package co.uk.redpixel.wardrobe.data.csv

trait CsvConverter[A] {

  type ErrorMessage = String

  def hasValidHeader(record: String): Boolean

  def convert(record: String): Either[ErrorMessage, A]

  def split(record: String): Array[String] =
    record.split(",").map(_.trim)
}
