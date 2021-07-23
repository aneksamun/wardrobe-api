package co.uk.redpixel.wardrobe.support

import co.uk.redpixel.wardrobe.data.Outfit
import co.uk.redpixel.wardrobe.data.search.{Limit, Offset}
import eu.timepit.refined.types.all.PosInt
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

package object scalacheck {

  def genNonEmptyString(gc: Gen[Char]): Gen[String] =
    Gen.choose(3, 8).flatMap(n => Gen.stringOfN(n, gc))

  def genNonPosNum[T](implicit num: Numeric[T], c: Choose[T]): Gen[T] =
    Gen.negNum[T].flatMap(n => Gen.choose(n, num.zero))

  def genNonNegNum[T](implicit num: Numeric[T], c: Choose[T]): Gen[T] =
    Gen.posNum[T].flatMap(n => Gen.choose(num.zero, n))

  def genPagingParams(size: PosInt): Gen[(Offset, Limit)] =
    for {
      limit     <- Gen.choose(min = 1, max = size.value).map(Limit(_))
      maxOffset =  (size.value / limit.value).asInstanceOf[Float].ceil.toInt - 1
      offset    <- Gen.choose(min = 0, maxOffset).map(Offset(_))
    } yield (offset, limit)

  def genOutfit: Gen[Outfit] =
    genNonEmptyString(Gen.alphaChar) map Outfit
}
