package dynver;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.FileTreeIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dynver.internal.Utils.getLast;
import static dynver.internal.Utils.hashMapCapacity;

public final class Main {
  private Main() {}

  public static void main(String[] args) throws IOException {
    final File repoDir = new File("target/test-repo");
    final Git git = Git.open(repoDir);
    final Repository repo = git.getRepository();
    final Version version = getVersion(repo);
    System.out.println("version = " + version);
  }

  public static Version getVersion(Repository repo) throws IOException {
    final ObjectId headId = repo.resolve(Constants.HEAD);

    final RevWalk revWalk = new RevWalk(repo);
    final RevCommit headCommit = revWalk.parseCommit(headId);
    revWalk.markStart(headCommit);

    final Map<String, Ref> tags = repo.getTags();
    final Set<String> headTags = new HashSet<>();
    for (Map.Entry<String, Ref> tagEntry : tags.entrySet()) {
      final String shortTagName = tagEntry.getKey();
      final Ref tag = tagEntry.getValue();
      final ObjectId tagId = tag.getObjectId();
      final RevCommit tagCommit = revWalk.parseCommit(tagId);
      if (tagCommit.equals(headId) && SemVer.parse(shortTagName) != null) {
        headTags.add(shortTagName);
      }
    }

    final boolean isDirty = new IndexDiff(repo, Constants.HEAD, new FileTreeIterator(repo)).diff();

    if (headTags.isEmpty()) {
      final String commitHash = headId.getName().substring(0, 12);
      final Map<RevCommit, String> tagsMap = new HashMap<>(hashMapCapacity(tags.size()));
      for (Map.Entry<String, Ref> tagEntry : tags.entrySet()) {
        final String shortTagName = tagEntry.getKey();
        final Ref tag = tagEntry.getValue();
        tagsMap.put(revWalk.parseCommit(tag.getObjectId()), shortTagName);
      }
      final List<RevCommit> commits = new ArrayList<>();
      for (RevCommit commit : revWalk) {
        commits.add(commit);
      }
      for (RevCommit commit : commits) {
        if (tagsMap.containsKey(commit)) {
          final String tagName = tagsMap.get(commit);
          final int tagDistance = commits.indexOf(commit);
          return new Version.PostTagVersion(tagName, tagDistance, commitHash, isDirty);
        }
      }
      return new Version.NoTagVersion(commitHash, isDirty);
    } else {
      return new Version.TaggedVersion(getLast(headTags), isDirty);
    }
  }
}
