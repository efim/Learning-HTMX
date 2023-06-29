
val toolkitV = "0.1.7"
val toolkit = "org.scala-lang" %% "toolkit" % toolkitV
val toolkitTest = "org.scala-lang" %% "toolkit-test" % toolkitV

ThisBuild / scalaVersion := "3.2.2"
libraryDependencies += toolkit
libraryDependencies += (toolkitTest % Test)
libraryDependencies +=      "org.thymeleaf" % "thymeleaf" % "3.1.1.RELEASE"
