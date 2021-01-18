package co.uk.redpixel.wardrobe.config

import java.net.URI
import co.uk.redpixel.wardrobe.config.DatabaseConfig._
import eu.timepit.refined.types.string.NonEmptyString

final case class DatabaseConfig(jdbcUrl: URI,
                                driverClassName: String,
                                user: UserName,
                                password: Password,
                                createSchema: Boolean,
                                threadPoolSize: Int)

object DatabaseConfig {

  type UserName = NonEmptyString
  type Password = NonEmptyString
}
