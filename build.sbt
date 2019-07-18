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

// LIGHTBEND TELEMETRY
// Enable the Lightbend Telemetry (Cinnamon) sbt plugin
lazy val app = project in file(".") enablePlugins (Cinnamon)

// Add the Cinnamon Agent for run and test
cinnamon in run := true
cinnamon in test := true

// Set the Cinnamon Agent log level
cinnamonLogLevel := "INFO"

// Use Coda Hale Metrics
libraryDependencies += Cinnamon.library.cinnamonCHMetrics
// Use Akka instrumentation
libraryDependencies += Cinnamon.library.cinnamonAkka
libraryDependencies += Cinnamon.library.cinnamonAkkaStream
// Use Akka HTTP instrumentation
libraryDependencies += Cinnamon.library.cinnamonAkkaHttp
// LIGHTBEND TELEMETRY

addCommandAlias("runAll", ";project service1; reStart; project service2; reStart; project akka-microservice-sample")
addCommandAlias("buildAll", ";project service1; docker:publishLocal; project service2; docker:publishLocal; project akka-microservice-sample" )
addCommandAlias("stopAll", "reStop")