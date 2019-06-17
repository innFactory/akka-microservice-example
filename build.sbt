name := "akka-microservice-sample"
version := "0.1"
scalaVersion := "2.13.0"



//** SERVICE 1 **//
lazy val service1 = project.in(file("service1"))


//** SERVICE 2 **//
lazy val service2 = project.in(file("service2"))

addCommandAlias("runAll", ";project service1; reStart; project service2; reStart; project akka-microservice-sample")
addCommandAlias("stopAll", ";project service1; reStop; project service2; reStop; project akka-microservice-sample")