package co.uk.redpixel.wardrobe.config

import co.uk.redpixel.wardrobe.config.ServerConfig.Port

final case class ServerConfig(httpPort: Port)

object ServerConfig {

  final case class Port(value: Int) {
    assert(value >= 0)
    assert(value <= 65535)
  }

  implicit def port2int(port: Port): Int = port.value
}
