package co.uk.redpixel.wardrobe.config

import eu.timepit.refined.types.all.NonSystemPortNumber

final case class ServerConfig(httpPort: NonSystemPortNumber)
