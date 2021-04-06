val Http4sVersion = "0.21.15"
val DoobieVersion = "0.10.0"
val CirceVersion = "0.14.0-M3"
val PureConfigVersion = "0.14.0"
val PureConfigRefinedVersion = "0.9.20"
val ScalaTestScalaCheckVersion = "3.1.0.0-RC2"
val ScalaCheckVersion = "1.15.2"
val ScalaTestVersion = "3.2.3"
val TestContainerVersion = "0.39.3"
val LogbackVersion = "1.2.3"
val FlywayVersion = "7.0.4"

lazy val root = (project in file("."))
  .settings(
    organization := "co.uk.redpixel",
    name := "wardrobe-api",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-blaze-server"             % Http4sVersion,
      "org.http4s"            %% "http4s-circe"                    % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"                      % Http4sVersion,
      "org.tpolecat"          %% "doobie-core"                     % DoobieVersion,
      "org.tpolecat"          %% "doobie-postgres"                 % DoobieVersion,
      "org.tpolecat"          %% "doobie-hikari"                   % DoobieVersion,
      "io.circe"              %% "circe-generic"                   % CirceVersion,
      "com.github.pureconfig" %% "pureconfig"                      % PureConfigVersion,
      "eu.timepit"            %% "refined-pureconfig"              % PureConfigRefinedVersion,
      "org.scalatest"         %% "scalatest"                       % ScalaTestVersion           % Test,
      "org.scalacheck"        %% "scalacheck"                      % ScalaCheckVersion          % Test,
      "org.scalatestplus"     %% "scalatestplus-scalacheck"        % ScalaTestScalaCheckVersion % Test,
      "com.dimafeng"          %% "testcontainers-scala-scalatest"  % TestContainerVersion       % Test,
      "com.dimafeng"          %% "testcontainers-scala-postgresql" % TestContainerVersion       % Test,
      "ch.qos.logback"        %  "logback-classic"                 % LogbackVersion,
      "org.flywaydb"          %  "flyway-core"                     % FlywayVersion
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
