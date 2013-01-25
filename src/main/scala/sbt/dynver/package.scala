package sbt

import util.matching.Regex

package object dynver {

  implicit class RichRegex(re: Regex) {
    def matches(s: String) = re.pattern.matcher(s).matches
  }


  implicit class RichString(str: String) {
    def orBlank: String = if (str == null) "" else str
  }

}
