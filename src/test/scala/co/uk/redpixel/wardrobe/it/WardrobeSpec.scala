package co.uk.redpixel.wardrobe.it

import co.uk.redpixel.wardrobe.support.fixture.WardrobeDatabase
import org.scalatest.BeforeAndAfterAll
import org.scalatest.featurespec.FixtureAsyncFeatureSpec
import org.scalatest.matchers.should.Matchers

class WardrobeSpec extends FixtureAsyncFeatureSpec
  with WardrobeDatabase
  with BeforeAndAfterAll
  with Matchers {
}
