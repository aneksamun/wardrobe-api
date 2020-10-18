package co.uk.redpixel.wardrobe.config

import java.net.URI

final case class DatabaseConfig(jdbcUrl: URI,
                                driverClassName: String,
                                user: UserName,
                                password: Password)
