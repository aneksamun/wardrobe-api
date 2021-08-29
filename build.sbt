val Http4sVersion = "0.21.15"
val DoobieVersion = "0.10.0"
val CirceVersion = "0.14.0-M3"
val PureConfigVersion = "0.16.0"
val PureConfigRefinedVersion = "0.9.20"
val SttpClientVersion = "3.3.6"
val ScalaTestScalaCheckVersion = "3.1.0.0-RC2"
val ScalaCheckVersion = "1.15.2"
val ScalaTestVersion = "3.2.3"
val TestContainerVersion = "0.39.3"
val LogbackVersion = "1.2.3"
val FlywayVersion = "7.0.4"

lazy val root = (project in file("."))
  .configs(IntegrationTest.extend(Test))
  .settings(
    Defaults.itSettings,
    organization := "co.uk.redpixel",
    name := "wardrobe-api",
    version := "1.0.0",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "org.http4s"                    %% "http4s-blaze-server"             % Http4sVersion,
      "org.http4s"                    %% "http4s-circe"                    % Http4sVersion,
      "org.http4s"                    %% "http4s-dsl"                      % Http4sVersion,
      "org.tpolecat"                  %% "doobie-core"                     % DoobieVersion,
      "org.tpolecat"                  %% "doobie-postgres"                 % DoobieVersion,
      "org.tpolecat"                  %% "doobie-hikari"                   % DoobieVersion,
      "io.circe"                      %% "circe-generic"                   % CirceVersion,
      "com.github.pureconfig"         %% "pureconfig"                      % PureConfigVersion,
      "eu.timepit"                    %% "refined-pureconfig"              % PureConfigRefinedVersion,
      "org.scalatest"                 %% "scalatest"                       % ScalaTestVersion           % "test,it",
      "org.scalacheck"                %% "scalacheck"                      % ScalaCheckVersion          % "test,it",
      "org.scalatestplus"             %% "scalatestplus-scalacheck"        % ScalaTestScalaCheckVersion % "test,it",
      "com.dimafeng"                  %% "testcontainers-scala-scalatest"  % TestContainerVersion       % "test,it",
      "com.dimafeng"                  %% "testcontainers-scala-postgresql" % TestContainerVersion       % "test,it",
      "com.softwaremill.sttp.client3" %% "core"                            % SttpClientVersion          % "test,it",
      "com.softwaremill.sttp.client3" %% "circe"                           % SttpClientVersion          % "test,it",
      "ch.qos.logback"                %  "logback-classic"                 % LogbackVersion,
      "org.flywaydb"                  %  "flyway-core"                     % FlywayVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    assembly / mainClass := Some("co.uk.redpixel.wardrobe.Main")
  )
  .enablePlugins(DockerPlugin)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-feature",
  "-Xfatal-warnings",
)

docker / dockerfile := {
  val artifact = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("adoptopenjdk/openjdk11:jdk-11.0.10_9-alpine")
    workDir("app")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
    expose(8080)
  }
}
