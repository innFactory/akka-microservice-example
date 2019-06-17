package de.innfactory.svc2

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.lightbend.rp.common.SocketBinding

object Service2 extends App {
  implicit val actorSystem = ActorSystem("payment")
  implicit val materializer = ActorMaterializer()

  println("Service 2 starting.")

  val routes =
    (get & pathEndOrSingleSlash) {
      complete("Service 2 is ok")
    }

  val host = SocketBinding.bindHost("http", default = "127.0.0.1")
  val port = SocketBinding.bindPort("http", default = 8081)
  Http().bindAndHandle(routes, host, port)
}
