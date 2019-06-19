package de.innfactory.common

import com.typesafe.config.ConfigFactory

trait Config {
  val config = ConfigFactory.load()

  lazy val service1Host = config.getString("service1.host")
  lazy val service1Port = config.getInt("service1.port")
  lazy val service1Grpc = config.getInt("service1.grpc")

  lazy val service2Host = config.getString("service2.host")
  lazy val service2Port = config.getInt("service2.port")
}
