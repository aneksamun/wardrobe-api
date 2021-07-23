package co.uk.redpixel.wardrobe.scenario

import cats.implicits.catsSyntaxOptionId
import cats.{Functor, Id}
import co.uk.redpixel.wardrobe.data.Clothes
import co.uk.redpixel.wardrobe.data.csv.instances._
import co.uk.redpixel.wardrobe.data.search.{Limit, Offset}
import co.uk.redpixel.wardrobe.fixture.WardrobeServices
import co.uk.redpixel.wardrobe.http.serdes.SearchPage
import co.uk.redpixel.wardrobe.support.Resources
import co.uk.redpixel.wardrobe.support.scalacheck._
import eu.timepit.refined.api.Refined
import io.circe.generic.auto._
import org.scalacheck.Gen
import org.scalatest.featurespec.AsyncFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{EitherValues, GivenWhenThen}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import sttp.client3.circe._
import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, UriContext, basicRequest, multipartFile}
import sttp.model.MediaType

class WardrobeSpec extends AsyncFeatureSpec
  with WardrobeServices
  with ScalaCheckPropertyChecks
  with GivenWhenThen
  with EitherValues
  with Resources
  with Matchers {

  Feature("Browse clothes") {

    info("As a customer")
    info("I wish to browse clothes")
    info("So that I can find an item I like")

    Scenario("Search clothes by name") {
      forAll(Gen.oneOf(clothes)) { selectedClothes =>
        When(s"""I search "${selectedClothes.name}" clothes""")
        val foundClothes = basicRequest
          .get(uri"http://$wardrobeApiUrl/api/clothes/${selectedClothes.name}")
          .response(asJson[Clothes])
          .send(usingSyncBackend)
          .body

        Then("I get expected ones")
        foundClothes.value should be(selectedClothes)
      }
    }

    Scenario("List clothes, categories & their outfits") {

      def expectedClothes(offset: Offset, limit: Limit) = {
        val skip = offset * limit
        val take = skip + limit
        clothes.slice(skip, take)
      }

      forAll(genPagingParams(size = Refined.unsafeApply(clothes.size))) { params: (Offset, Limit) =>
        When(s"I search clothes with offset ${params._1} and limit ${params._2}")
        val collectedClothes = basicRequest
          .get(uri"http://$wardrobeApiUrl/api/clothes?offset=${params._1}&limit=${params._2}")
          .response(asJson[SearchPage])
          .send(usingSyncBackend)
          .body

        Then("I find expected set of the clothes")
        collectedClothes.value should be(SearchPage(
          expectedClothes(params._1, params._2),
          total = clothes.size
        ))
      }
    }
  }

  Feature("Edit clothes") {

    info("As a customer")
    info("I wish to tag clothes")
    info("So that I can specify the outfit they belongs to")

    Scenario("Tag clothes") {
      forAll(Gen.oneOf(clothes), genOutfit) { (clothes, outfit) =>
        Given(s"""an outfit with the name "${outfit.name}""""")
        When(s"""I make request to update outfit for the "${clothes.name}" clothes""")
        val updatedClothes = basicRequest
          .put(uri"http://$wardrobeApiUrl/api/clothes/${clothes.name}/outfit")
          .body(outfit)
          .response(asJson[Clothes])
          .send(usingSyncBackend)
          .body

        Then("it gets updated")
        updatedClothes.value should be(clothes.copy(outfit = outfit.some))
      }
    }
  }

  private lazy val clothes = load[Clothes]("clothing.csv").sortBy(_.name)

  private val usingSyncBackend: SttpBackend[Identity, Any] =
    HttpURLConnectionBackend()

  def loadData(): Unit = {
    Functor[Id].map(loadFile("clothing.csv"))(clothingFile =>
      basicRequest
        .post(uri"http://$wardrobeApiUrl/api/clothes")
        .multipartBody(
          multipartFile("clothing", clothingFile)
            .fileName(clothingFile.getName)
            .contentType(MediaType.TextCsv)
        )
        .send(usingSyncBackend)
    )
  }
}
