package semver4j;

import java.util.regex.Pattern;

// Inspired by Guava and Scala
final class Utils {
  private Utils() {}

  static int requireNonNegative(final int integer) {
    require(integer >= 0);
    return integer;
  }

  static String requireMatches(final String input, final Pattern pattern) {
    require(pattern.matcher(input).matches());
    return input;
  }

  static void require(final boolean requirement) {
    if (!requirement) {
      throw new IllegalArgumentException("requirement failed");
    }
  }

  static String nullToBlank(final String str) {
    return str == null ? "" : str;
  }
}
