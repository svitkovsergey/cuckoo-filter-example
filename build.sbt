import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := "streaming-example",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.12",
    libraryDependencies ++= compileDependencies,
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    resolvers ++= Seq(
      "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
    ),
    scalacOptions ++= Seq(
      "-language:higherKinds"
    ),
    dockerExposedPorts := Seq(8080),
    fork in run := true,
    javaOptions += "-Xmx2G"
  ).enablePlugins(JavaAppPackaging)