package co.uk.redpixel.wardrobe.data

import org.slf4j.LoggerFactory

import scala.annotation.implicitNotFound

package object csv {

  private lazy val logger = LoggerFactory.getLogger("CSV converter")

  @implicitNotFound("No CSV file converter found for ${A}")
  def convert[A](records: Seq[String])(implicit converter: CsvConverter[A]): Seq[A] = {
    records match {
      case x +: xs if converter.hasValidHeader(x) && xs.nonEmpty =>
        xs.filter(!_.isBlank)
          .map(converter.convert)
          .map {
            case Right(clothes) => Some(clothes)
            case Left(error) =>
              logger.warn(s"Error parsing CSV record: $error")
              None
          }
          .filter(_.isDefined)
          .map(_.get)
      case _ => Seq.empty
    }
  }

  object syntax {

    implicit class StringSequenceOps(val records: Seq[String]) extends AnyVal {
      def into[A](implicit viaConverter: CsvConverter[A]): Seq[A] =
        convert(records)(viaConverter)
    }
  }

  object instances {
    implicit val clothesCsvConverter = new ClothesCsvConverter
  }
}
