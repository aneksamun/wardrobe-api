package co.uk.redpixel.wardrobe.support

import org.scalatest.Suite

import scala.io.Source
import scala.util.Using

trait Resources {
  this: Suite =>

  def loadResource(resource: String): Seq[String] = {
    Using.resource(getClass.getResourceAsStream(s"/$resource")) {
      Source.fromInputStream(_).getLines().toSeq
    }
  }
}
