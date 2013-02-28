package dynver;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Version {
  private Version() {}

  public abstract boolean isDirty();

  protected final String suffix = isDirty() ? "+" + new SimpleDateFormat("yyyyMMdd").format(new Date()) : "";

  public static final class TaggedVersion extends Version {
    public final String tag;
    public final boolean isDirty;

    public TaggedVersion(String tag, boolean isDirty) {
      this.tag = tag;
      this.isDirty = isDirty;
    }

    @Override
    public boolean isDirty() {
      return isDirty;
    }

    @Override
    public String toString() {
      return tag + suffix;
    }
  }

  public static final class PostTagVersion extends Version {
    public final String tag;
    public final int tagDistance;
    public final String commitHash;
    public final boolean isDirty;

    public PostTagVersion(String tag, int tagDistance, String commitHash, boolean isDirty) {
      this.tag = tag;
      this.tagDistance = tagDistance;
      this.commitHash = commitHash;
      this.isDirty = isDirty;
    }

    @Override
    public boolean isDirty() {
      return isDirty;
    }

    @Override
    public String toString() {
      return tag + "+" + tagDistance + "-" + commitHash + suffix;
    }
  }

  public static final class NoTagVersion extends Version {
    public final String commitHash;
    public final boolean isDirty;

    public NoTagVersion(String commitHash, boolean isDirty) {
      this.commitHash = commitHash;
      this.isDirty = isDirty;
    }

    @Override
    public boolean isDirty() {
      return isDirty;
    }

    @Override
    public String toString() {
      return commitHash + suffix;
    }
  }
}
