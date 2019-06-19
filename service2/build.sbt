name := "service-2"

libraryDependencies ++= ReflectionResolver.getDefaultDependencies()

enablePlugins(AkkaGrpcPlugin)