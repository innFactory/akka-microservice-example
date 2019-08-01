name := "service-1"

libraryDependencies ++= ReflectionResolver.getDefaultDependencies()

// Kamon

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-bundle" % "2.0.0",
  "io.kamon" %% "kamon-prometheus" % "2.0.0",
  "io.kamon" %% "kamon-zipkin" % "2.0.0"
)

// Kamon

enablePlugins(AkkaGrpcPlugin)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

packageName in Docker := "innfactory-test/service1"
version in Docker := "0.10"
dockerExposedPorts := Seq(2552, 8558)