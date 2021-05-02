package co.uk.redpixel.wardrobe.support.fixture

//import cats.{Functor, Id}
//import cats.effect.IO
//import co.uk.redpixel.wardrobe.config.DatabaseConfig
//import co.uk.redpixel.wardrobe.persistence.Database
import com.dimafeng.testcontainers.PostgreSQLContainer
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import org.scalatest.{FixtureAsyncTestSuite, FutureOutcome}
import org.testcontainers.utility.DockerImageName

//import java.net.URI

trait WardrobeDatabase extends TestContainerForAll {
  this: FixtureAsyncTestSuite =>

  type FixtureParam = PostgreSQLContainer

  val containerDef: PostgreSQLContainer.Def = PostgreSQLContainer.Def(
    dockerImageName = DockerImageName.parse("postgres:13.1-alpine"),
    databaseName = "wardrobe",
    username = "user",
    password = "1234"
  )

  def withFixture(test: OneArgAsyncTest): FutureOutcome = withContainers { databaseContainer =>
    test apply databaseContainer
  }

  override def afterContainersStart(db: PostgreSQLContainer): Unit = {
//    Functor[Id].map(DatabaseConfig(
//      URI.create(db.jdbcUrl),
//      db.driverClassName,
//      db.username,
//      db.password,
//      createSchema = true,
//      threadPoolSize = 32)
//    )()
//
//    Database.connect[IO]()
  }
}
