name := "service-2"

libraryDependencies ++= ReflectionResolver.getDefaultDependencies()

enablePlugins(AkkaGrpcPlugin)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)


packageName in Docker := "innfactory-test/service2"
version in Docker := "0.2"
dockerExposedPorts := Seq(2552, 8558, 8090)