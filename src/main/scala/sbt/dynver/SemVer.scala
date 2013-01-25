package sbt.dynver

object SemVer {
  val groupingRegex = ("^" +
    """((\d+)\.(\d+)\.(\d+))""" + // version string
    """(?:\-([\dA-Za-z\-]+(?:\.[\dA-Za-z\-]+)*))?""" + // prerelease suffix (opt)
    """(?:\+([\dA-Za-z\-]+(?:\.[\dA-Za-z\-]+)*))?""" + // build suffix (opt)
    "$").r

  private[SemVer] val suffixReGroup = """(?:[\dA-Za-z\-]+(?:\.[\dA-Za-z\-]+)*)"""
  val matchingRegex = ("""^\d+\.\d+\.\d+""" + // version string
    """(?:\-""" + suffixReGroup + """)?""" + // prerelease suffix (opt)
    """(?:\+""" + suffixReGroup + """)?""" + // build suffix (opt)
    "$").r

  def unapply(str: String): Option[SemVer] = str match {
    case SemVer.groupingRegex(_, major, minor, patch, prerelease, build) =>
      Some(SemVer(major.toInt, minor.toInt, patch.toInt, prerelease.orBlank, build.orBlank))
    case _ => None
  }
}

case class SemVer(major: Int, minor: Int = 0, patch: Int = 0, prerelease: String = "", build: String = "") {
  require(major >= 0)
  require(minor >= 0)
  require(patch >= 0)

  require(prerelease != null)
  require(build != null)

  val suffixRe = (SemVer.suffixReGroup + '?').r
  require(suffixRe.matches(prerelease), s"'$prerelease' doesn't match $suffixRe")
  require(suffixRe.matches(build), s"'$build' doesn't match $suffixRe")
}
