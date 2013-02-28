package dynver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dynver.internal.Utils.*;

public final class SemVer {
  public static final Pattern groupingRegex = Pattern.compile("^" +
      "((\\d+)\\.(\\d+)\\.(\\d+))" + // version string
      "(?:\\-([\\dA-Za-z\\-]+(?:\\.[\\dA-Za-z\\-]+)*))?" + // prerelease suffix (opt)
      "(?:\\+([\\dA-Za-z\\-]+(?:\\.[\\dA-Za-z\\-]+)*))?" + // build suffix (opt)
      "$");

  private static final String suffixReGroup = "(?:[\\dA-Za-z\\-]+(?:\\.[\\dA-Za-z\\-]+)*)";

  private static final Pattern suffixRe = Pattern.compile(suffixReGroup + '?');

  public static final Pattern matchingRegex = Pattern.compile("^" +
      "\\d+\\.\\d+\\.\\d+" + // version string
      "(?:\\-" + suffixReGroup + ")?" + // prerelease suffix (opt)
      "(?:\\+" + suffixReGroup + ")?" + // build suffix (opt)
      "$");

  public final int major;
  public final int minor;
  public final int patch;
  public final String prerelease;
  public final String build;

  public SemVer(int major, int minor, int patch, String prerelease, String build) {
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
