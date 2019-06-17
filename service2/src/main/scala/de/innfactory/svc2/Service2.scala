package de.innfactory.svc2

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers
import akka.stream.ActorMaterializer
import com.lightbend.rp.common.SocketBinding
import de.innfactory.svc1.grpc._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Service2 extends App {
  println("Service 2 starting.")

  implicit val actorSystem = ActorSystem("service2")
  implicit val materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  val routes =
    (get & pathEndOrSingleSlash) {
      complete("Service 2 is ok")
    } ~ get{
      pathPrefix("greet") {
        path(PathMatchers.Segment) { name =>
          complete(askService1(name).map(m => s"Hello ${m.message} - The actorsystem greeted you ${m.greeted} times!"))
        }
      }
    }

  val host = SocketBinding.bindHost("http", default = "127.0.0.1")
  val port = SocketBinding.bindPort("http", default = 8090)
  Http().bindAndHandle(routes, host, port)


  val clientSettings = GrpcClientSettings.fromConfig(
    clientName = "project.WithSpecificConfiguration")

  val client: GreeterService = GreeterServiceClient(clientSettings)

  def askService1(name: String) = {
    actorSystem.log.info("Performing request")
    val reply = client.sayHello(HelloRequest(name))
    reply.onComplete {
      case Success(msg) =>
        println(s"Service 1 send reply: $msg")
      case Failure(e) =>
        println(s"Error sayHello to service 1: $e")
    }
    reply
  }


}
