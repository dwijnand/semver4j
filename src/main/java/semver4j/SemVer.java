package semver4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static semver4j.Utils.nullToBlank;
import static semver4j.Utils.requireMatches;
import static semver4j.Utils.requireNonNegative;

public final class SemVer {
  public static final Pattern groupingRegex = Pattern.compile('^'
      + "((\\d+)\\.(\\d+)\\.(\\d+))" // version string
      + "(?:\\-([\\dA-Za-z\\-]+(?:\\.[\\dA-Za-z\\-]+)*))?" // prerelease suffix (optional)
      + "(?:\\+([\\dA-Za-z\\-]+(?:\\.[\\dA-Za-z\\-]+)*))?" // build suffix (optional)
      + '$');

  private static final String suffixReGroup = "(?:[\\dA-Za-z\\-]+(?:\\.[\\dA-Za-z\\-]+)*)";

  private static final Pattern suffixRe = Pattern.compile(suffixReGroup + '?');

  public static final Pattern matchingRegex = Pattern.compile('^'
      + "\\d+\\.\\d+\\.\\d+" // version string
      + "(?:\\-" + suffixReGroup + ")?" // prerelease suffix (optional)
      + "(?:\\+" + suffixReGroup + ")?" // build suffix (optional)
      + '$');

  public final int major;
  public final int minor;
  public final int patch;
  public final String prerelease;
  public final String build;

  public SemVer(final int major, final int minor, final int patch, final String prerelease, final String build) {
    this.major = requireNonNegative(major);
    this.minor = requireNonNegative(minor);
    this.patch = requireNonNegative(patch);
    this.prerelease = requireMatches(requireNonNull(prerelease), suffixRe);
    this.build = requireMatches(requireNonNull(build), suffixRe);
  }

  public static SemVer parse(final String str) {
    final Matcher matcher = groupingRegex.matcher(str);
    if (matcher.matches()) {
      final int major = Integer.parseInt(matcher.group(2));
      final int minor = Integer.parseInt(matcher.group(3));
      final int patch = Integer.parseInt(matcher.group(4));
      final String prerelease = nullToBlank(matcher.group(5));
      final String build = nullToBlank(matcher.group(6));
      return new SemVer(major, minor, patch, prerelease, build);
    }
    return null;
  }
}
