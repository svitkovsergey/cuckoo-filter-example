import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := "streaming-example",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.3",
    libraryDependencies ++= compileDependencies,
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    dockerExposedPorts := Seq(8080)
  ).enablePlugins(JavaAppPackaging)

scalacOptions ++= Seq(
  "-language:higherKinds"
)