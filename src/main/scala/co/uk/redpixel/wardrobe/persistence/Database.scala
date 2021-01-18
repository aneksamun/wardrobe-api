package co.uk.redpixel.wardrobe.persistence

import cats.Monad
import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import cats.implicits.{catsSyntaxApplicativeId, _}
import co.uk.redpixel.wardrobe.config.DatabaseConfig
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import scala.util.Try

object Database {

  type MigrationResult = Either[Throwable, MigrateResult]

  def createSchema[F[_]](config: DatabaseConfig)
                        (xa: HikariTransactor[F])
                        (implicit F: Sync[F]): F[Option[MigrationResult]] = {
    def migrateSchema() = {
      xa.configure { dataSource =>
        Try(Flyway.configure()
          .dataSource(dataSource)
          .load()
          .migrate()).toEither.some.pure[F]
      }
    }

    Monad[F].ifM(F.pure(config.createSchema))(migrateSchema(), F.pure(None))
  }

  def connect[F[_] : Async](config: DatabaseConfig)
                           (implicit C: ContextShift[F]): Resource[F, HikariTransactor[F]] = {
    for {
      ec <- ExecutionContexts.fixedThreadPool[F](config.threadPoolSize)
      blocker <- Blocker[F]
      xa <- HikariTransactor.newHikariTransactor[F](
        driverClassName = config.driverClassName,
        url = config.jdbcUrl.toString,
        user = config.user.value,
        pass = config.password.value,
        connectEC = ec,
        blocker = blocker
      )
    } yield xa
  }
}
