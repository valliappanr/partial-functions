name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml_2.12
libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.0-RC2"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.6.6",

  // [Optional]
  "io.kamon" %% "kamon-statsd" % "0.6.6",
  // [Optional]
  "io.kamon" %% "kamon-datadog" % "0.6.6",

  "io.kamon" %% "kamon-log-reporter" % "0.6.6"

  // ... and so on with all the modules you need.
)
