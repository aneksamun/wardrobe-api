package co.uk.redpixel.wardrobe.data.search

import cats.data.NonEmptyList
import co.uk.redpixel.wardrobe.data.validation.FieldError
import co.uk.redpixel.wardrobe.support.scalacheck.genNonNegNum
import org.scalacheck.Gen
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class OffsetSpec extends AnyWordSpec
  with ScalaCheckPropertyChecks
  with Matchers {

  "The offset validation" should {
    "succeed for a non negative value" in {
      forAll(genNonNegNum[Int].map(Offset(_))) { offset =>
        Offset.validator.validate(offset) shouldBe None
      }
    }

    "fail for a negative number" in {
      forAll(Gen.negNum[Int].map(Offset(_))) { offset =>
        val error = FieldError("offset", "Must be non negative")
        Offset.validator.validate(offset) shouldBe Some(NonEmptyList.of(error))
      }
    }
  }
}
