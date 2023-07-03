ThisBuild / scalaVersion := "3.2.2"
fork := true

ThisBuild / version := "0.0.1"
ThisBuild / organization := "industries.sunshine"

val toolkitV = "0.1.7"
val toolkit = "org.scala-lang" %% "toolkit" % toolkitV
val toolkitTest = "org.scala-lang" %% "toolkit-test" % toolkitV

lazy val orderSummaryComponent = (project in file("."))
  .settings(
    name := "order-summary-component",
    libraryDependencies += toolkit,
    libraryDependencies += (toolkitTest % Test),
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.1",
      "com.lihaoyi" %% "mainargs" % "0.5.0",
      "org.thymeleaf" % "thymeleaf" % "3.1.1.RELEASE"
    )
  )
