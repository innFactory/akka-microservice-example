package de.innfactory.svc1

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.lightbend.rp.common.SocketBinding

object Service1 extends App {

  implicit val actorSystem = ActorSystem("payment")
  implicit val materializer = ActorMaterializer()

  println("Service 1 starting.")

  val routes =
    (get & pathEndOrSingleSlash) {
      complete("Service 1 is ok")
    }

  val host = SocketBinding.bindHost("http", default = "127.0.0.1")
  val port = SocketBinding.bindPort("http", default = 8080)
  Http().bindAndHandle(routes, host, port)
}
