package co.uk.redpixel.wardrobe.data

import org.slf4j.LoggerFactory

import scala.annotation.implicitNotFound

package object csv {

  private lazy val logger = LoggerFactory.getLogger("CSV converter")

  @implicitNotFound("No CSV file converter found for ${A}")
  def convert[A](records: Vector[String])(implicit converter: CsvConverter[A]): Vector[A] = {
    records match {
      case x +: xs if converter.hasValidHeader(x) && xs.nonEmpty =>
        xs.filter(!_.isBlank)
          .map(converter.convert)
          .map {
            case Right(clothes) => Some(clothes)
            case Left(error) =>
              logger.warn(s"Error parsing CSV record: ${error.getMessage}")
              None
          }
          .filter(_.isDefined)
          .map(_.get)
      case _ => Vector.empty
    }
  }

  object syntax {

    implicit class StringSequenceOps(val records: Vector[String]) extends AnyVal {
      def to[A](implicit viaConverter: CsvConverter[A]): Vector[A] =
        convert(records)(viaConverter)
    }
  }

  object instances {
    implicit val clothesCsvConverter = new ClothesCsvConverter
  }
}
