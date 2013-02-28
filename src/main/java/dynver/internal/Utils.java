package dynver.internal;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

// Inspired by Guava and Scala
public final class Utils {
  public static int requireNonNegative(int integer) {
    require(integer >= 0);
    return integer;
  }

  public static <T> T requireNonNull(T obj) {
    require(obj != null);
    return obj;
  }

  public static String requireMatches(String input, Pattern pattern) {
    require(pattern.matcher(input).matches());
    return input;
  }

  public static void require(boolean requirement) {
    if (!requirement) {
      throw new IllegalArgumentException("requirement failed");
    }
  }

  public static String nullToBlank(String str) {
    return str == null ? "" : str;
  }

  public static <T> T getLast(Iterable<T> iterable) {
    if (iterable instanceof List) {
      final List<T> list = (List<T>) iterable;
      return list.get(list.size() - 1);
    }
    return getLast(iterable.iterator());
  }

  public static <T> T getLast(Iterator<T> iterator) {
    while (true) {
      final T current = iterator.next();
      if (!iterator.hasNext()) {
        return current;
      }
    }
  }

  // Taken from Guava Ints
  public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

  // Taken from Guava Maps
  public static int hashMapCapacity(int expectedSize) {
    if (expectedSize < 3) {
      require(expectedSize >= 0);
      return expectedSize + 1;
    }
    if (expectedSize < MAX_POWER_OF_TWO) {
      return expectedSize + expectedSize / 3;
    }
    return Integer.MAX_VALUE; // any large value
  }
}
