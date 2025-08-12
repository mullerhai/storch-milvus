import sbt.*
import Keys.*
import sbt.Def.settings

import scala.collection.immutable.Seq

ThisBuild / tlBaseVersion := "0.1.1-2.6.1" // your current series x.y
//ThisBuild / CoursierCache := file("D:\\coursier")
ThisBuild / organization := "io.github.mullerhai" //"dev.storch"
ThisBuild / organizationName := "storch.dev"
ThisBuild / startYear := Some(2024)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("mullerhai", "mullerhai")
)
ThisBuild / version := "0.1.1-2.6.1"

ThisBuild / scalaVersion := "3.6.4"
ThisBuild / tlSonatypeUseLegacyHost := false

import xerial.sbt.Sonatype.sonatypeCentralHost
ThisBuild / sonatypeCredentialHost := sonatypeCentralHost

import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommandAndRemaining("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges,
)

lazy val root = (project in file("."))
  .settings(
    name := "storch-milvus"
  )
ThisBuild / tlSitePublishBranch := Some("main")
ThisBuild / scmInfo := Some( ScmInfo( url( "https://github.com/mullerhai/storch-milvus" ), "scm:git:https://github.com/mullerhai/storch-milvus.git" ) )
ThisBuild / apiURL := Some(new URL("https://storch.dev/api/"))
libraryDependencies += "com.google.api.grpc" % "proto-google-common-protos" % "2.54.1"
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "1.0.0-alpha.1"
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % "1.0.0-alpha.1"
//// (optional) If you need scalapb/scalapb.proto or anything from
//// google/protobuf/*.proto
//PB.protoSources in Compile := Seq(file("resources/proto"))

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value
)
libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
)

ThisBuild  / assemblyMergeStrategy := {
  case v if v.contains("main$package$.class") => MergeStrategy.first
  case v if v.contains("main.class")          => MergeStrategy.first
  case v if v.contains("main$package.class")  => MergeStrategy.first
  case v if v.contains("main$package.tasty")  => MergeStrategy.first
  case v if v.contains("main.tasty")          => MergeStrategy.first
  case v if v.contains("module-info.class")   => MergeStrategy.discard
  case v if v.contains("UnusedStub")          => MergeStrategy.first
  case v if v.contains("aopalliance")         => MergeStrategy.first
  case v if v.contains("inject")              => MergeStrategy.first
  case v if v.contains("jline")               => MergeStrategy.discard
  case v if v.contains("scala-asm")           => MergeStrategy.discard
  case v if v.contains("asm")                 => MergeStrategy.discard
  case v if v.contains("scala-compiler")      => MergeStrategy.deduplicate
  case v if v.contains("reflect-config.json") => MergeStrategy.discard
  case v if v.contains("jni-config.json")     => MergeStrategy.discard
  case v if v.contains("git.properties")      => MergeStrategy.discard
  case v if v.contains("reflect.properties")      => MergeStrategy.discard
  case v if v.contains("compiler.properties")      => MergeStrategy.discard
  case v if v.contains("scala-collection-compat.properties")      => MergeStrategy.discard
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

