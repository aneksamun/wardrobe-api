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

  "The CSV header check" should {
    "succeed for a valid record" in new Scope {
      converter.hasValidHeader("name, category") shouldBe true
    }

    "fail for a invalid header" in new Scope {
      forAll(Gen.listOf(genNonEmptyString(Gen.alphaChar))) { titles =>
        converter.hasValidHeader(titles.mkString(",")) shouldBe false
      }
    }
  }

  "The clothes CSV file" should {
    "be successfully parsed" in new Scope {
      forAll(Gen.listOfN(2, genNonEmptyString(Gen.alphaChar))) {
        case entries @ x :: xs :: Nil =>
          converter
            .convert(entries.mkString(","))
            .contains(Clothes(x, Category(xs))) shouldBe true
        case _ => fail()
      }
    }

    "fail to parse" in new Scope {
      forAll(Gen.listOfN(1, genNonEmptyString(Gen.alphaChar))) { entries =>
        converter.convert(entries.mkString(",")).isLeft shouldBe true
      }
    }
  }

  trait Scope {
    val converter = new ClothesCsvConverter
  }
}
