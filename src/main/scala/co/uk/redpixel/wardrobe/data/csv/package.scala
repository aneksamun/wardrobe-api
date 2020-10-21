package co.uk.redpixel.wardrobe.data

import org.slf4j.LoggerFactory

import scala.annotation.implicitNotFound

package object csv {

  implicit val clothesCsvConverter = new ClothesCsvConverter

  implicit class StringSequenceOps(data: Seq[String]) {

    lazy val logger = LoggerFactory.getLogger("CSV converter")

    @implicitNotFound("No CSV file converter found for entity")
    def as[A](implicit converter: CsvConverter[A]): Seq[A] = {
      data match {
        case x +: xs if converter.hasValidHeader(x) && xs.nonEmpty =>
          xs.map(converter.convert)
            .map {
              case Right(clothes) => Some(clothes)
              case Left(error) =>
                logger.warn(s"Error parsing CSV line: ${error.getMessage}")
                None
            }
            .filter(_.isDefined)
            .map(_.get)
        case _ => Seq.empty
      }
    }
  }
}
