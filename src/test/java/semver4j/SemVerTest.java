package semver4j;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public final class SemVerTest {
  private static final List<String> good = Arrays.asList(
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
  );

  private static final List<String> bad = Arrays.asList(
      "v1.0.0",
      "a.b.c",
      "1",
      "1.0.0b",
      "1.0",
      "1.0.0+b[\\]^_`uild", // [,\,],^,_,` are between A-z, but not A-Za-z
      "1.0.0+build-acbe.", // trailing period
      "1.0.0+build.!@#$%"
  );

  private final String versionString;
  private final boolean result;

  public SemVerTest(final String versionString, final boolean result) {
    this.versionString = versionString;
    this.result = result;
  }

  @Test
  public void testParse() {
    if (result) {
      assertNotNull(SemVer.parse(versionString));
    } else {
      assertNull(SemVer.parse(versionString));
    }
  }

  @Test
  public void testMatching() {
    assertEquals(result, SemVer.matches(versionString));
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {
    return ImmutableList.copyOf(Iterables.concat(
        FluentIterable.from(good).transform(getTransformFunc(true)),
        FluentIterable.from(bad).transform(getTransformFunc(false))));
  }

  private static Function<String, Object[]> getTransformFunc(final boolean result) {
    return new Function<String, Object[]>() {
      @Nullable @Override public Object[] apply(@Nullable final String versionStr) {
        return new Object[]{versionStr, result};
      }
    };
  }
}
