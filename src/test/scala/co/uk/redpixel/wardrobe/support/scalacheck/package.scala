package co.uk.redpixel.wardrobe.support

import org.scalacheck.Gen

package object scalacheck {

  def genNonEmptyString(gc: Gen[Char]): Gen[String] =
    Gen.choose(3, 8).flatMap(n => Gen.stringOfN(n, gc))
}
