package co.uk.redpixel.wardrobe.data.csv

trait CsvConverter[A] {

  def hasValidHeader(record: String): Boolean

  def convert(record: String): Either[Throwable, A]

  def split(record: String): Array[String] =
    record.split(",")
          .map(_.trim)
}
