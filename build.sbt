name := "FreeMonads"

version := "1.0"

scalaVersion := "2.12.1"

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.9.0",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
        