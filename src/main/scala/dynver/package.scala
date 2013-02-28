import util.matching.Regex

package object dynver {
  implicit class RichRegex(re: Regex) {
    def matches(str: String) = re.pattern.matcher(str).matches
  }


  implicit class RichString(str: String) {
    def orBlank: String = if (str == null) "" else str
  }
}
