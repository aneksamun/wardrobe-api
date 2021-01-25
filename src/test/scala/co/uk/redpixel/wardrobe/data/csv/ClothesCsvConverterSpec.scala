package co.uk.redpixel.wardrobe.data.csv

import co.uk.redpixel.wardrobe.data.{Category, Clothes}
import co.uk.redpixel.wardrobe.support.scalacheck.genNonEmptyString
import org.scalacheck.Gen
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ClothesCsvConverterSpec extends AnyWordSpec
  with ScalaCheckPropertyChecks
  with Matchers {

  "The clothes CSV converter" should {
    "determine a valid header" in new Scope {
      converter.hasValidHeader("name, category") shouldBe true
    }

    "convert a valid record" in new Scope {
      forAll(Gen.listOfN(2, genNonEmptyString(Gen.alphaChar))) {
        case entries @ x :: xs :: Nil =>
          converter
            .convert(entries.mkString(","))
            .contains(Clothes(x, Category(xs))) shouldBe true
        case _ => fail()
      }
    }

    "determine a bad header" in new Scope {
      forAll(Gen.listOf(Gen.alphaStr)) { titles =>
        converter.hasValidHeader(titles.mkString(",")) shouldBe false
      }
    }
  }

  trait Scope {
    val converter = new ClothesCsvConverter
  }
}
