package co.uk.redpixel.wardrobe.support

import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

package object scalacheck {

  def genNonEmptyString(gc: Gen[Char]): Gen[String] =
    Gen.choose(3, 8).flatMap(n => Gen.stringOfN(n, gc))

  def genNonPosNum[T](implicit num: Numeric[T], c: Choose[T]): Gen[T] =
    Gen.negNum[T].flatMap(n => Gen.choose(n, num.zero))

  def genNonNegNum[T](implicit num: Numeric[T], c: Choose[T]): Gen[T] =
    Gen.posNum[T].flatMap(n => Gen.choose(num.zero, n))
}
