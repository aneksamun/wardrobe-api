package co.uk.redpixel.wardrobe.scenario

import co.uk.redpixel.wardrobe.fixture.WardrobeServices
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpec
import org.scalatest.matchers.should.Matchers

class WardrobeSpec extends AsyncFeatureSpec
  with WardrobeServices
  with GivenWhenThen
  with Matchers {

  Feature("Browse clothes") {

    info("As a customer")
    info("I wish to browse clothes")
    info("So that I can find an item I like")

    Scenario("Search clothes by name") {
      "a" should be ("a")
    }

    Scenario("List clothes, categories & their outfits") {
      "a" should be ("a")
    }
  }

  Feature("Edit clothes") {

    info("As a customer")
    info("I wish to tag clothes")
    info("So that I can specify the outfit they belongs to")

    Scenario("Tag clothes") {
      "a" should be ("a")
    }
  }

  def loadData(): Unit = {
    ???
  }
}






































