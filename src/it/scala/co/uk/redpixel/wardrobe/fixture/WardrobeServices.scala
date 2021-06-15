package co.uk.redpixel.wardrobe.fixture

import co.uk.redpixel.wardrobe.fixture.WardrobeServices.{WardrobeService, WardrobeDatabase}
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService}
import org.scalatest.AsyncTestSuite
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File

trait WardrobeServices extends TestContainerForAll {
  this: AsyncTestSuite =>

  lazy val wardrobeApiUrl: String = withContainers { containers =>
    val host = containers.getServiceHost(WardrobeService.name, WardrobeService.port)
    val port = containers.getServicePort(WardrobeService.name, WardrobeService.port)
    s"$host:$port"
  }

  val containerDef = DockerComposeContainer.Def(
    composeFiles = new File("././docker-compose.test.yml"),
    exposedServices = Seq(
      WardrobeService,
      WardrobeDatabase
    )
  )

  override def afterContainersStart(containers: DockerComposeContainer): Unit = {
    super.afterContainersStart(containers)
    loadData()
  }

  def loadData(): Unit
}

object WardrobeServices {

  private val WardrobeService = ExposedService("wardrobeApi_1", 8080,  Wait.forHttp("/internal/status"))
  private val WardrobeDatabase = ExposedService("postgres_1", 5432)
}
