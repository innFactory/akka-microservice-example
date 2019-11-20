name := "service-1"

libraryDependencies ++= ReflectionResolver.getDefaultDependencies()

enablePlugins(AkkaGrpcPlugin)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

// LIGHTBEND TELEMETRY
// Use Coda Hale Metrics
libraryDependencies += Cinnamon.library.cinnamonCHMetrics
// Use Akka instrumentation
libraryDependencies += Cinnamon.library.cinnamonAkka
libraryDependencies += Cinnamon.library.cinnamonAkkaStream
// Use Akka HTTP instrumentation
libraryDependencies += Cinnamon.library.cinnamonAkkaHttp

libraryDependencies += Cinnamon.library.cinnamonPrometheus
libraryDependencies += Cinnamon.library.cinnamonPrometheusHttpServer

libraryDependencies += Cinnamon.library.cinnamonJvmMetricsProducer

enablePlugins (Cinnamon)

// Add the Cinnamon Agent for run and test
cinnamon in run := true
cinnamon in test := true

// Set the Cinnamon Agent log level
cinnamonLogLevel := "INFO"
// LIGHTBEND TELEMETRY

packageName in Docker := "innfactory-test/service1"
version in Docker := "0.1"
dockerExposedPorts := Seq(2552, 8558)