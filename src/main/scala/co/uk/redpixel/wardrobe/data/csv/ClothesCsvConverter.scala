package co.uk.redpixel.wardrobe.data.csv

import co.uk.redpixel.wardrobe.data.{Category, Clothes}

class ClothesCsvConverter extends CsvConverter[Clothes] {

  def hasValidHeader(record: String): Boolean =
    split(record).containsSlice(Seq("name", "category"))

  def convert(record: String): Either[ErrorMessage, Clothes] = {
    split(record) match {
      case Array(clothesName, categoryName) =>
        Right(Clothes(clothesName, Category(categoryName)))
      case _ => Left("The record has no expected size of the fields")
    }
  }
}
