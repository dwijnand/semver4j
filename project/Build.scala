import sbt._
import Keys._

object Build extends sbt.Build {
  lazy val root = Project(id = "sbt-dynver", base = file("."))
}
