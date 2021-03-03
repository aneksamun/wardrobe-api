package co.uk.redpixel.wardrobe.data.csv.parsing

import co.uk.redpixel.wardrobe.data.{Category, Clothes}
import co.uk.redpixel.wardrobe.data.csv.instances._
import co.uk.redpixel.wardrobe.data.csv.syntax._
import co.uk.redpixel.wardrobe.support.Resources
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FileParsingSpec extends AnyWordSpec
  with Resources
  with Matchers {

  "The clothes parsing" should {

    "succeed for a valid CSV file" in {
      loadResource("clothing.csv").into[Clothes] should be(Seq(
        Clothes("iSwim Summer Bikini", Category("Bikinis")),
        Clothes("iWalk Blue Jeans", Category("Trousers")),
        Clothes("iWalk Dress Trousers", Category("Trousers")),
        Clothes("iWalk Long White Dress", Category("Dresses")),
        Clothes("Nice™ Yellow Shirt", Category("Tops")),
        Clothes("Nice™ Green T", Category("Tops")),
        Clothes("iRun Black Trainers", Category("Shoes")),
        Clothes("iRun White Trainers", Category("Shoes")),
        Clothes("Elégant Handcrafted Clogs", Category("Shoes"))
      ))
    }

    "skip invalid records" in {
      loadResource("bad.csv").into[Clothes] should be(Seq.empty)
    }

    "skip irrelevant files" in {
      loadResource("username.csv").into[Clothes] should be(Seq.empty)
    }
  }
}
