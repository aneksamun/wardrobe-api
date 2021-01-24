package co.uk.redpixel.wardrobe.data.csv

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ClothesCsvConverterSpec extends AnyFreeSpec
  with ScalaCheckPropertyChecks
  with Matchers {

  "The clothes CSV converter" - {
    "should determine a valid header" in new Scope {
      converter.hasValidHeader("name, category") should be (true)
    }

    "determine a bad header" in new Scope {

    }
  }

  trait Scope {
    val converter = new ClothesCsvConverter
  }
}
