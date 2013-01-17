import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import collection.JavaConverters._
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{IndexDiff, Constants}
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.FileTreeIterator

object Version {
  def main(args: Array[String]) {
    val repoDir = new File("target/test-repo")
    println("version = " + getVersion(Git.open(repoDir)))
  }

  def getVersion(git: Git): String = {
    val repo = git.getRepository
    val headId = repo.resolve(Constants.HEAD)

    val revWalk = new RevWalk(repo)
    val headCommit = revWalk.parseCommit(headId)
    revWalk.markStart(headCommit)

    val tags = repo.getTags.asScala.toMap
    val headTags = for {
      (shortTagName, tag) <- tags
      tagId = tag.getObjectId
      revObj = revWalk.parseAny(tagId)
      peeledRevObj = revWalk.peel(revObj)
      if peeledRevObj == headId
    // TODO: Match some regex, see semver
    } yield shortTagName

    val version =
      if (headTags.isEmpty) {
        val tagsMap = for {(shortTagName, tag) <- tags} yield (revWalk.parseCommit(tag.getObjectId), shortTagName)
        val commitSeq = revWalk.asScala.toSeq
        val commit = commitSeq.find(tagsMap.contains).get
        val tagName = tagsMap(commit)
        val tagDistance = commitSeq.indexOf(commit)

        tagName + '+' + tagDistance + '-' + headId.name.slice(0, 12)
      } else headTags.last

    val isDirty = new IndexDiff(repo, Constants.HEAD, new FileTreeIterator(repo)).diff()

    if (isDirty) version + "+" + new SimpleDateFormat("yyyyMMdd").format(new Date())
    else version
  }
}
