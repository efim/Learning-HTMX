scalaVersion := "3.3.0"
fork := true

ThisBuild / version := "0.0.1"
ThisBuild / organization := "industries.sunshine"

lazy val root = (project in file("."))
  .settings(
    name := "priceGrid",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.1",
      "com.lihaoyi" %% "scalatags" % "0.12.0",
      "com.lihaoyi" %% "mainargs" % "0.5.0"
    ),
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.1" % Test,
      "com.lihaoyi" %% "scalatags" % "0.12.0" % Test,
      "com.lihaoyi" %% "mainargs" % "0.5.0" % Test
    )
  )
