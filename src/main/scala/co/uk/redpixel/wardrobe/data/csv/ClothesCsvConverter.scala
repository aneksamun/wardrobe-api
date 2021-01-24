package co.uk.redpixel.wardrobe.data.csv

import co.uk.redpixel.wardrobe.data.{Category, Clothes}

import scala.util.Try

class ClothesCsvConverter extends CsvConverter[Clothes] {

  def hasValidHeader(record: String): Boolean =
    split(record).containsSlice(Seq("name", "category"))

  def convert(record: String): Either[Throwable, Clothes] =
    Try {
      val entries = split(record)
      Clothes(
        name = entries.head,
        category = Category(name = entries.last)
      )
    }.toEither
}
