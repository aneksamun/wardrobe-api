package co.uk.redpixel.wardrobe.data.search

import cats.data.NonEmptyList
import co.uk.redpixel.wardrobe.data.validation.FieldError
import co.uk.redpixel.wardrobe.support.scalacheck.genNonPosNum
import org.scalacheck.Gen
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class LimitSpec extends AnyWordSpec
  with ScalaCheckPropertyChecks
  with Matchers {

  "The limit validation" should {
    "succeed for a value greater than 0" in {
      forAll(Gen.posNum[Int].map(Limit(_))) { limit =>
        Limit.validator.validate(limit) shouldBe None
      }
    }

    "fail for a valid less or equal to 0" in {
      forAll(genNonPosNum[Int].map(Limit(_))) { limit =>
        val error = FieldError("limit", "Must be greater than 0")
        Limit.validator.validate(limit) shouldBe Some(NonEmptyList.of(error))
      }
    }
  }
}
