import sbt._
import Keys._

object Build extends sbt.Build {
  lazy val root = Project(id = "semver4j", base = file("."))
}
