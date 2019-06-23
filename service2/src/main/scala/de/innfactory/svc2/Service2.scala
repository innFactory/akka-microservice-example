package de.innfactory.svc2

import akka.actor.ActorSystem
import akka.discovery.kubernetes.KubernetesApiServiceDiscovery
import akka.discovery.{Discovery, ServiceDiscovery}
import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.management.scaladsl.AkkaManagement
import akka.stream.ActorMaterializer
import de.innfactory.common.Config
import de.innfactory.svc1.grpc._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object Service2 extends App with Config {
  println("Service 2 starting.")

  implicit val actorSystem = ActorSystem("service2")
  implicit val materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  // Akka Management hosts the HTTP routes used by bootstrap
  AkkaManagement(actorSystem).start()

  val discovery = Discovery(actorSystem).loadServiceDiscovery("kubernetes-api")

  println("Try to resolve gRPC endpoint for service 1")
  val lookup: Future[ServiceDiscovery.Resolved] = discovery.lookup("service1", 20 seconds)

  val r = Await.result(lookup, 20 seconds)
  val grpcHost = r.addresses.head.address.get.toString.replace("/","")

  println("resolved:")
  println(r)
  println("addresses:")
  println(r.addresses)

  println(s"Resolved gRPC Endpoints >$grpcHost:$service1Grpc<")


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


  Http().bindAndHandle(
    routes,
    service2Host,
    service2Port)

  val clientSettings = GrpcClientSettings.connectToServiceAt(grpcHost, service1Grpc).withTls(false).withDeadline(2 seconds).withUserAgent("service2")


    /*.fromConfig(
    clientName = "project.WithSpecificConfiguration").with*/

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
