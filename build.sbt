ThisBuild / scalaVersion := "3.2.2"
fork := true

ThisBuild / version := "0.0.1"
ThisBuild / organization := "industries.sunshine"

lazy val rockPaperScissors = (project in file("14-rock-paper-scissors"))
lazy val testimonialsGrid = (project in file("13-testimonials-grid-section"))
lazy val orderSummaryComponent = (project in file("12-order-summary-component-thymeleaf"))
lazy val singlePriceGridComponent = (project in file("11-single-price-grid-component"))

lazy val aggregatedExercises = (project in file("."))
  .settings(
    name := "aggregated-htmx-exercises",
  ).dependsOn(
    rockPaperScissors,
    testimonialsGrid,
    orderSummaryComponent,
    singlePriceGridComponent,
  )
