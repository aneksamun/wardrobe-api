package co.uk.redpixel.wardrobe.config

import java.net.URI

import co.uk.redpixel.wardrobe.config.DatabaseConfig._

final case class DatabaseConfig(jdbcUrl: URI,
                                driverClassName: String,
                                user: UserName,
                                password: Password,
                                createSchema: Boolean,
                                threadPoolSize: Int)

object DatabaseConfig {

  final case class UserName(value: String) extends AnyVal

  final case class Password(value: String) extends AnyVal
}
