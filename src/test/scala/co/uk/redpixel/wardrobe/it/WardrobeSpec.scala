package co.uk.redpixel.wardrobe.it

import co.uk.redpixel.wardrobe.support.fixture.WardrobeDatabase
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.FixtureAsyncFeatureSpec
import org.scalatest.matchers.should.Matchers

class WardrobeSpec extends FixtureAsyncFeatureSpec
  with WardrobeDatabase
  with BeforeAndAfterAll
  with GivenWhenThen
  with Matchers {

  // TODO:
  // 1. Give an in info
  // 2. Check routes to use endpoint
  // 3. Think about parsing configuration (maybe to use a docker compose)?
  // 4. Init schema and load data once container started

  info("")
  info("")
  info("")


  Feature("") {

  }

}
