name := "service-1"

libraryDependencies ++= ReflectionResolver.getDefaultDependencies()

enablePlugins(AkkaGrpcPlugin)
