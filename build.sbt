name := "home_security_system"

version := "1.0"

lazy val root = (project in file("."))
  .enablePlugins(UniversalPlugin, PlayJava, JavaServerAppPackaging)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.11.8"

mainClass in Compile := Some("play.core.server.ProdServerStart")

libraryDependencies ++= {
  val akkaV = "2.4.16"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,

    "com.pi4j" % "pi4j-core" % "1.1",
    "com.pi4j" % "pi4j-core" % "1.1",
    "com.pi4j" % "pi4j-gpio-extension" % "1.1",
    "com.pi4j" % "pi4j-device" % "1.1",
    javaWs   )
}

routesGenerator := InjectedRoutesGenerator


