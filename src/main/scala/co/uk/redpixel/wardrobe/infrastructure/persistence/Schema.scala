package co.uk.redpixel.wardrobe.infrastructure.persistence

import cats.Applicative
import cats.implicits.catsSyntaxApplicativeId
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import scala.util.Try

object Schema {

  type MigrationResult = Either[Throwable, MigrateResult]

  def migrate[F[_]: Applicative](transactor: HikariTransactor[F]): F[MigrationResult] = {
    transactor.configure { dataSource =>
      Try(Flyway.configure()
        .dataSource(dataSource)
        .load()
        .migrate()).toEither.pure[F]
    }
  }
}
