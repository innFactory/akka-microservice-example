package de.innfactory.svc1

import akka.actor.{Actor, Props, ReceiveTimeout}
import de.innfactory.svc1.grpc._

import akka.actor.{Props, ReceiveTimeout}
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.Passivate
import akka.actor.{Actor, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}

import scala.concurrent.duration._

class GreeterActor extends Actor {

  var state : Map[String, Int] = Map()

  override def receive: Receive = {
    case HelloRequest(from: String) => {
      val newCount = state.getOrElse(from, 0) + 1

      state = state.filter(_._1 == from).+((from, newCount))
      sender ! HelloReply(from, newCount)
    }
  }
}

object ShardedGreeters {
  def props = Props(new ShardedGreeters)
  def name = "sharded-users"
}

class ShardedGreeters extends Actor {

  ClusterSharding(context.system).start(
    ShardedGreeter.shardName,
    ShardedGreeter.props,
    ClusterShardingSettings(context.system),
    ShardedGreeter.extractEntityId,
    ShardedGreeter.extractShardId
  )

  def shardedGreeters = {
    ClusterSharding(context.system).shardRegion(ShardedGreeter.shardName)
  }

  def receive = {
    case cmd: HelloRequest     => shardedGreeters forward cmd
  }
}

object ShardedGreeter {
  def props = Props(new ShardedGreeter)

  case object StopGreeters

  val shardName: String = "greeters"

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: HelloReply     => (cmd.message, cmd)
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: HelloReply =>
      (math.abs(cmd.message.hashCode) % 12).toString
  }

}

class ShardedGreeter extends GreeterActor {
  import ShardedGreeter._

  context.setReceiveTimeout(10.seconds)

  override def unhandled(msg: Any) = msg match {
    case ReceiveTimeout =>
      context.parent ! Passivate(stopMessage = ShardedGreeter.StopGreeters)
    case StopGreeters => context.stop(self)
    case _ => println("unhandeled sharded role")
  }
}