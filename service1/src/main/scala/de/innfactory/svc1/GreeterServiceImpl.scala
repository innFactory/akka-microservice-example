package de.innfactory.svc1

import akka.actor.ActorRef
import akka.stream.Materializer
import de.innfactory.svc1.grpc._

import scala.concurrent.Future
import akka.pattern._
import akka.util.Timeout


class GreeterServiceImpl(greeterRef : ActorRef)(implicit mat: Materializer, timeout: Timeout) extends GreeterService {

  override def sayHello(in: HelloRequest): Future[HelloReply] = {
    println(s"sayHello to ${in.name}")
    (greeterRef ? in).mapTo[HelloReply]
  }

}