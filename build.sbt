import com.lightbend.lagom.sbt.LagomImport._
import com.lightbend.lagom.sbt.LagomJava

name := "politikei-lagom"

version := "1.0"

scalaVersion in ThisBuild := "2.11.8"

lazy val voteApi = project("vote-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi
  )

lazy val voteImpl = project("vote-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomJavadslPersistence,
      lagomJavadslTestKit
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(voteApi, utils)

lazy val utils = project("utils")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi
  )

def project(id: String) = Project(id, base = file(id))
  .settings(
    scalacOptions in Compile += "-Xexperimental" // this enables Scala lambdas to be passed as Java SAMs
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.3" // actually, only api projects need this
    )
  )