package semver4j;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

@SuppressWarnings("InstanceMethodNamingConvention")
public final class UtilsTest {
  @Test(expected = IllegalArgumentException.class)
  public void requireNonNegative_throwsIllegalArgumentException_onNegative() {
    Utils.requireNonNegative(-1);
  }

  @Test
  public void requireNonNegative_returnsParameter_onNonNegative() {
    assertEquals(1, Utils.requireNonNegative(1));
  }

  @Test
  public void requireNonNegative_returnsParameter_onZero() {
    assertEquals(0, Utils.requireNonNegative(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void requireMatches_throwsIllegalArgumentException_onMismatch() {
    Utils.requireMatches("abc", Pattern.compile("def"));
  }

  @Test
  public void requireMatches_returnsParameter_onMatch() {
    assertEquals("abc", Utils.requireMatches("abc", Pattern.compile("[a-z]+")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void require_throwsIllegalArgumentException_onFalse() {
    Utils.require(false);
  }

  @Test
  public void require_succeeds_onTrue() {
    Utils.require(true);
  }

  @Test
  public void nullToBlank_returnsBlank_onNull() {
    assertEquals("", Utils.nullToBlank(null));
  }

  @Test
  public void nullToBlank_returnsBlank_onBlank() {
    assertEquals("", Utils.nullToBlank(""));
  }

  @Test
  public void nullToBlank_returnsParameter_onNonBlank() {
    assertEquals("a", Utils.nullToBlank("a"));
  }
}
