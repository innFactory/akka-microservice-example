addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "0.6.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.22")

// LIGHTBEND TELEMETRY
addSbtPlugin("com.lightbend.cinnamon" % "sbt-cinnamon" % "2.11.3")
credentials += Credentials(Path.userHome / ".lightbend" / "commercial.credentials")
resolvers += Resolver.url("lightbend-commercial", url("https://repo.lightbend.com/commercial-releases"))(Resolver.ivyStylePatterns)
// LIGHTBEND TELEMETRY