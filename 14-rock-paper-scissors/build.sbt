ThisBuild / scalaVersion := "3.2.2"
fork := true

ThisBuild / version := "0.0.1"
ThisBuild / organization := "industries.sunshine"

lazy val rockPaperScissors = (project in file("."))
  .settings(
    name := "rock-paper-scissors",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.1",
      "com.lihaoyi" %% "mainargs" % "0.5.0",
      "org.thymeleaf" % "thymeleaf" % "3.1.1.RELEASE",
    )
  )
