val Http4sVersion = "0.21.8"
val DoobieVersion = "0.9.2"
val CirceVersion = "0.13.0"
val PureConfigVersion = "0.14.0"
val LogbackVersion = "1.2.3"
val FlywayVersion = "7.0.4"
val Specs2Version = "4.10.0"

lazy val root = (project in file("."))
  .settings(
    organization := "co.uk.redpixel",
    name := "wardrobe-api",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.13.3",
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"            %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"            %% "http4s-circe"        % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"          % Http4sVersion,
      "org.tpolecat"          %% "doobie-core"         % DoobieVersion,
      "org.tpolecat"          %% "doobie-postgres"     % DoobieVersion,
      "org.tpolecat"          %% "doobie-hikari"       % DoobieVersion,
      "io.circe"              %% "circe-generic"       % CirceVersion,
      "com.github.pureconfig" %% "pureconfig"          % PureConfigVersion,
      "org.specs2"            %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"        %  "logback-classic"     % LogbackVersion,
      "org.flywaydb"          %  "flyway-core"         % FlywayVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-feature",
  "-Xfatal-warnings",
)
