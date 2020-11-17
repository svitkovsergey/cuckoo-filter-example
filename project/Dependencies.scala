import sbt._

object Versions {
  val Http4sVersion = "0.21.8"
  val CirceVersion = "0.13.0"
  val Specs2Version = "4.10.5"
  val LogbackVersion = "1.2.3"
  val CatsCoreVersion = "2.1.0"
}

object Dependencies {
  import Versions._

  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % Http4sVersion
  val http4sCirce = "org.http4s" %% "http4s-circe" % Http4sVersion
  val http4sDSL = "org.http4s" %% "http4s-dsl" % Http4sVersion
  val circeGeneric = "io.circe" %% "circe-generic" % CirceVersion
  val logback = "ch.qos.logback" % "logback-classic" % LogbackVersion
  val catsCore = "org.typelevel" %% "cats-core" % CatsCoreVersion
  val circeCore = "io.circe" % "circe-core_2.13" % CirceVersion
  val circeParser = "io.circe" % "circe-parser_2.13" % CirceVersion

  lazy val compileDependencies = Seq(
      catsCore, circeCore, circeParser, http4sBlazeClient, http4sBlazeServer, http4sCirce, http4sDSL, circeGeneric, logback
    )

  val scalatestCore = "org.scalatest" % "scalatest-core_2.13" % "3.2.0-M4" % Test
  // https://mvnrepository.com/artifact/org.scalatest/scalatest-flatspec
  val scalatestFlatspec = "org.scalatest" % "scalatest-flatspec_2.13" % "3.2.0-M4" % Test
  // https://mvnrepository.com/artifact/org.scalatest/scalatest-matchers-core
  val scalatestMatchers = "org.scalatest" % "scalatest-matchers-core_2.13" % "3.2.0-M4" % Test

  lazy val testDependencies =
    Seq(scalatestCore, scalatestFlatspec, scalatestMatchers)
}

