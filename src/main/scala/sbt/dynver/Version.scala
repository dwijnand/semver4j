package sbt.dynver

import java.util.Date
import java.text.SimpleDateFormat

sealed trait Version {
  def isDirty: Boolean

  protected val suffix = if (isDirty) "+" + new SimpleDateFormat("yyyyMMdd").format(new Date()) else ""
}

case class TaggedVersion(tag: String, isDirty: Boolean = false) extends Version {
  override lazy val toString = tag + suffix
}

case class TagAndCommitVersion(tag: String, tagDistance: Int, commitHash: String, isDirty: Boolean = false)
    extends Version {
  override lazy val toString = tag + "+" + tagDistance + "-" + commitHash + suffix
}

case class CommitVersion(commitHash: String, isDirty: Boolean = false)
    extends Version {
  override lazy val toString = commitHash + suffix
}
