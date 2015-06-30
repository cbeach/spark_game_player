import sbt._
import Keys._

lazy val root = (project in file(".")).
    settings(
        name := "spark_game_player",
        version := "1.0",
        scalaVersion := "2.11.4",
        libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.3.1" % "provided",
        libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.1" % "provided"
    )
