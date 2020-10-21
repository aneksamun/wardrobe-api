package co.uk.redpixel.wardrobe.data.csv

trait CsvConverter[A] {

  def hasValidHeader(s: String): Boolean

  def convert(s: String): Either[Throwable, A]

  def splitLine(line: String): Array[String] = line.split(",").map(_.trim)
}
