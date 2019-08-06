name := "service-2"

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

packageName in Docker := "innfactory-test/service2"
version in Docker := "0.3"
dockerExposedPorts := Seq(2552, 8558, 8090)