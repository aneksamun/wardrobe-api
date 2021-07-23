package co.uk.redpixel.wardrobe.support

import co.uk.redpixel.wardrobe.data.csv.CsvConverter
import co.uk.redpixel.wardrobe.data.csv.syntax.StringSequenceOps
import org.scalatest.Suite

import java.io.File
import scala.io.Source
import scala.util.Using

trait Resources {
  this: Suite =>

  def load[A](resource: String)(implicit converter: CsvConverter[A]): Seq[A] =
    Using.resource(getClass.getResourceAsStream(s"/$resource")) {
      Source.fromInputStream(_).getLines().toSeq
    }.as[A]

  def loadFile(resource: String) = new File(getClass.getResource(s"/$resource").toURI)
}
