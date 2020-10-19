package co.uk.redpixel.wardrobe.config

import java.net.URI

import co.uk.redpixel.wardrobe.domain.{Password, UserName}

final case class DatabaseConfig(jdbcUrl: URI,
                                driverClassName: String,
                                user: UserName,
                                password: Password,
                                createSchema: Boolean,
                                threadPoolSize: Int)
