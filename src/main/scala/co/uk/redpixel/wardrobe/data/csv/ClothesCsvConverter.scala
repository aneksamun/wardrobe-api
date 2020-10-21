package co.uk.redpixel.wardrobe.data.csv

import co.uk.redpixel.wardrobe.data.{Category, Clothes}

import scala.util.Try

class ClothesCsvConverter extends CsvConverter[Clothes] {

  def hasValidHeader(s: String): Boolean = {
    val headers = splitLine(s)
    headers.headOption.contains("name") && headers.lastOption.contains("category")
  }

  def convert(s: String): Either[Throwable, Clothes] =
    Try {
      val data = splitLine(s)
      Clothes(
        name = data.head,
        category = data.lastOption.map(Category)
      )
    }.toEither

}
