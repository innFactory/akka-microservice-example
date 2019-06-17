package de.innfactory.common

import com.typesafe.config.ConfigFactory

trait Config {
  val config = ConfigFactory.load

  lazy val serviceHost = config.getString("service.host")
  lazy val servicePort = config.getInt("service.port")
  lazy val serviceGrpc = config.getInt("service.host")
}
