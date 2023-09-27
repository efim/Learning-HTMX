name := "countries-page"

val toolkitV = "0.1.7"
val toolkit = "org.scala-lang" %% "toolkit" % toolkitV
val toolkitTest = "org.scala-lang" %% "toolkit-test" % toolkitV

val cask = "com.lihaoyi" %% "cask" % "0.9.1"
val mainargs = "com.lihaoyi" %% "mainargs" % "0.5.4"
val requests = "com.lihaoyi" %% "requests" % "0.8.0"

ThisBuild / scalaVersion := "3.2.2"
libraryDependencies ++= Seq(cask, mainargs, requests)
libraryDependencies += toolkit
libraryDependencies += (toolkitTest % Test)

// https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf
libraryDependencies += "org.thymeleaf" % "thymeleaf" % "3.1.2.RELEASE"
