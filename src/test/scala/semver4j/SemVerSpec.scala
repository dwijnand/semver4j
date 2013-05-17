package semver4j

import org.specs2.mutable.Specification

class SemVerSpec extends Specification {
  val good = Traversable(
    "1.0.8",
    "1.23.7",
    "2.0.0-alpha.123.abc",
    "2.0.0-alpha.123.abc+build.acebfde1284",
    "1.0.0-alpha",
    "1.0.0-alpha.1",
    "1.0.0-0.3.7",
    "1.0.0-x.7.z.92",
    "1.0.0-alpha",
    "1.0.0-alpha.1",
    "1.0.0-beta.2",
    "1.0.0-beta.11",
    "1.0.0-rc.1",
    "1.0.0-rc.1+build.1",
    "1.0.0-rc.1+build.1-b",
    "1.0.0",
    "1.0.0+0.3.7",
    "1.3.7+build",
    "1.3.7+build.2.b8f12d7",
    "1.3.7+build.11.e0f985a",
    "1.3.7+build.11.e0f9-85a",
    "1.0.0+build-acbe",
    "2.0.0+build.acebfde1284-alpha.123.abc"
  )
  val bad = Traversable(
    "v1.0.0",
    "a.b.c",
    "1",
    "1.0.0b",
    "1.0",
    "1.0.0+b[\\]^_`uild", // [,\,],^,_,` are between A-z, but not A-Za-z
    "1.0.0+build-acbe.", // trailing period
    "1.0.0+build.!@#$%"
  )

  "Good version strings" should {
    "be defined for SemVer" in {
      good forall (str => (SemVer.parse(str) must not).beNull)
    }
    "match matchingRegex" in {
      good forall (str => str must be matching(SemVer.matchingRegex))
    }
  }

  "Bad version strings" should {
    "not be defined for SemVer" in {
      bad forall (str => SemVer.parse(str) must beNull)
    }
    "not match matchingRegex" in {
      bad forall (str => str must not be matching(SemVer.matchingRegex))
    }
  }
}
