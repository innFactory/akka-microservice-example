package de.innfactory.svc1

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.UseHttp2.Always
import akka.http.scaladsl.{Http, HttpConnectionContext}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.lightbend.rp.common.SocketBinding
import de.innfactory.svc1.grpc.GreeterServiceHandler
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.util.Timeout
import de.innfactory.common.Config

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object Service1 extends App with Config {
  println("Service 1 starting.")


  implicit val actorSystem = ActorSystem("service1")
  implicit val materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val timeout: Timeout = Timeout(2 seconds)


  val shardedGreeter = actorSystem.actorOf(ShardedGreeter.props, ShardedGreeter.shardName)


  val routes =
    (get & pathEndOrSingleSlash) {
      complete("Service 1 is ok")
    }

  val intraService: HttpRequest => Future[HttpResponse] =
    GreeterServiceHandler(new GreeterServiceImpl(shardedGreeter))

  val host = SocketBinding.bindHost("http", default = "127.0.0.1")
  val httpPort = SocketBinding.bindPort("http", default = 8080)
  val grpcPort = SocketBinding.bindPort("grpc", default = 8081)

  Http().bindAndHandle(
    routes,
    host,
    httpPort)

  Http().bindAndHandleAsync(
    intraService,
    host,
    grpcPort,
    connectionContext = HttpConnectionContext(http2 = Always))
}
