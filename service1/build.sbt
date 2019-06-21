name := "service-1"

libraryDependencies ++= ReflectionResolver.getDefaultDependencies()

enablePlugins(AkkaGrpcPlugin)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

packageName in Docker := "innfactory-test/service1"
version in Docker := "0.1"
dockerExposedPorts := Seq(2552, 8558)