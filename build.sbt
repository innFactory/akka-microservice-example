name := "akka-microservice-sample"
version := "0.1"
scalaVersion := "2.13.0"

//** COMMON **//
lazy val common = project.in(file("common"))

//** SERVICE 1 **//
lazy val service1api = project.in(file("service1-api"))
lazy val service1 = project.in(file("service1"))
  .dependsOn(service1api)
  .dependsOn(common)


//** SERVICE 2 **//
lazy val service2 = project.in(file("service2"))
  .dependsOn(service1api)
  .dependsOn(common)

addCommandAlias("runAll", ";project service1; reStart; project service2; reStart; project akka-microservice-sample")
addCommandAlias("buildAll", ";project service1; docker:publishLocal; project service2; docker:publishLocal; project akka-microservice-sample" )
addCommandAlias("stopAll", "reStop")