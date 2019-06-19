package de.innfactory.svc1

import akka.actor.{ActorSystem, Props}
import akka.discovery.Discovery
import akka.http.scaladsl.UseHttp2.Always
import akka.http.scaladsl.{Http, HttpConnectionContext}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
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

  val serviceDiscovery = Discovery(actorSystem).discovery


  val shardedGreeter = actorSystem.actorOf(ShardedGreeter.props, ShardedGreeter.shardName)


  val routes =
    (get & pathEndOrSingleSlash) {
      complete("Service 1 is ok")
    }

  val intraService: HttpRequest => Future[HttpResponse] =
    GreeterServiceHandler(new GreeterServiceImpl(shardedGreeter))


  Http().bindAndHandle(
    routes,
    service1Host,
    service1Port)

  Http().bindAndHandleAsync(
    intraService,
    service1Host,
    service1Grpc,
    connectionContext = HttpConnectionContext(http2 = Always))
}
