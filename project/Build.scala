import sbt._
import Keys._
import sbtassembly.AssemblyKeys._
import sbtassembly.PathList
import sbtassembly.MergeStrategy


object Build extends Build {

  val akkaVersion  = "2.3.3"
  val sprayVersion = "1.3.1"
  val sparkVersion = "1.6.1"
  connectInput in run := true

  val sharedSettings: Seq[sbt.Project.Setting[_]] = Seq(
    organization := "com.africastalking",
    version      := "0.1.0",
    scalaVersion := "2.10.4",
    resolvers += Resolver.bintrayRepo("galarragas", "maven"),
    resolvers ++= Seq(
      "RoundHeights"  at "http://maven.spikemark.net/roundeights",
      "rediscala" at "http://dl.bintray.com/etaty/maven",
      "Artima Maven Repository" at "http://repo.artima.com/releases",
      "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked"
    )
  )

  lazy val elmer = Project(
    id            = "elmer",
    base          = file("."),
    settings      = Project.defaultSettings ++ sharedSettings
  ).settings(
    name := "elmer"
  ).aggregate(
    elmerCore,
    elmerAPI,
    elmerLearn
  )

  lazy val elmerCore = Project(
    id                = "elmer-core",
    base              = file("elmer-core"),
    settings          = Project.defaultSettings ++
      sharedSettings
  ).settings(
    name := "elmer-core",
    libraryDependencies ++= Seq(
      "io.spray"                      %%  "spray-json"       % "1.2.6",
      "com.typesafe.akka"             %%  "akka-actor"       % akkaVersion,
      "com.typesafe.akka"             %%  "akka-slf4j"       % akkaVersion,
      "ch.qos.logback"                %   "logback-core"     % "1.1.2",
      "ch.qos.logback"                %   "logback-classic"  % "1.1.2",
      "com.typesafe"                  %   "config"           % "1.2.0",
      "com.github.mauricio"           %   "mysql-async_2.10" % "0.2.18",
      "org.xerial.snappy"             %   "snappy-java"      % "1.1.0.1",
      "net.jpountz.lz4"               %   "lz4"              % "1.2.0",
      "com.github.nscala-time"        %%  "nscala-time"      % "1.0.0",
      "com.etaty.rediscala"           %%  "rediscala"        % "1.3.1",
      "com.roundeights"               %%  "hasher"           % "1.0.0",
      "commons-daemon"                %   "commons-daemon"   % "1.0.15",
      "io.spray"                      %   "spray-client"     % sprayVersion,
      "io.spray"                      %   "spray-httpx"      % sprayVersion,
      "com.typesafe.akka"             %%  "akka-testkit"     % akkaVersion   % "test",
      "org.specs2"                    %%  "specs2"           % "2.2.3"       % "test",
      "org.scalactic"                 %%  "scalactic"        % "2.2.6",
      "com.pragmasoft"                %%  "spray-funnel"     % "1.1-spray1.3",
      "net.databinder.dispatch"       %%  "dispatch-core"    % "0.11.2",
      "org.scalatest"                 %%  "scalatest"        % "2.2.6"       % "test"
    )
  )

  lazy val elmerLearn = Project(
    id                 = "elmer-learn",
    base               = file("elmer-learn"),
    settings           = Project.defaultSettings ++
      sharedSettings
  ).settings(
    name := "elmer-learn",
    test in assembly := {},
    libraryDependencies ++= Seq(
      "org.scalatest"     %% "scalatest"    % "2.2.6"      % "test",
      "org.apache.spark"  %% "spark-core"   % sparkVersion,
      "org.apache.spark"  %% "spark-mllib"  % sparkVersion
    )
  ).dependsOn(
    elmerCore
  )

  lazy val elmerAPI = Project(
    id = "elmer-api",
    base = file("elmer-api"),
    settings = Project.defaultSettings ++
      sharedSettings
  ).settings(
    name := "elmer-api",
    test in assembly := {},
    libraryDependencies ++= Seq(
      "org.scalactic"     %% "scalactic"           % "2.2.6",
      "io.spray"          %   "spray-can"          % sprayVersion,
      "io.spray"          %   "spray-routing"      % sprayVersion,
      "io.spray"          %   "spray-testkit"      % sprayVersion  % "test",
      "com.typesafe.akka" %%  "akka-testkit"       % akkaVersion   % "test"
    )
  ).dependsOn(
    elmerCore,
    elmerLearn
  )

  assemblyMergeStrategy in assembly := {
    case PathList("org", "apache", xs @ _*) => MergeStrategy.last
    case PathList("com", "google", xs @ _*) => MergeStrategy.last
    case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
    case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
    case PathList("org", "spark", xs @ _*) => MergeStrategy.last
    case PathList("stax", "stax-api", xs @ _*) => MergeStrategy.last
    case "about.html" => MergeStrategy.rename
    case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
    case "META-INF/mailcap" => MergeStrategy.last
    case "META-INF/mimetypes.default" => MergeStrategy.last
    case "plugin.properties" => MergeStrategy.last
    case "log4j.properties" => MergeStrategy.last
    case _ => MergeStrategy.last
  }

}
