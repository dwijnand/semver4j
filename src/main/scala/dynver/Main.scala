package dynver

import java.io.File
import collection.JavaConverters._
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{Repository, IndexDiff, Constants}
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.FileTreeIterator

object Main {
  def main(args: Array[String]) {
    val repoDir = new File("target/test-repo")
    val git = Git.open(repoDir)
    val repo = git.getRepository
    val version = getVersion(repo)
    println("version = " + version)
  }

  def getVersion(repo: Repository, tagPred: (String => Boolean) = SemVer.validate): Version = {
    val headId = repo.resolve(Constants.HEAD)

    val revWalk = new RevWalk(repo)
    val headCommit = revWalk.parseCommit(headId)
    revWalk.markStart(headCommit)

    val tags = repo.getTags.asScala.toMap
    val headTags = for {
      (shortTagName, tag) <- tags
      tagId = tag.getObjectId
      tagCommit = revWalk.parseCommit(tagId)
      if tagCommit == headId
      if tagPred(shortTagName)
    } yield shortTagName

    val isDirty = new IndexDiff(repo, Constants.HEAD, new FileTreeIterator(repo)).diff()

    if (headTags.isEmpty) {
      val commitHash = headId.name.slice(0, 12)
      val tagsMap = for {(shortTagName, tag) <- tags} yield (revWalk.parseCommit(tag.getObjectId), shortTagName)
      val commitSeq = revWalk.asScala.toSeq
      commitSeq.find(tagsMap.contains) match {
        case Some(commit) => {
          val tagName = tagsMap(commit)
          val tagDistance = commitSeq.indexOf(commit)
          PostTagVersion(tagName, tagDistance, commitHash, isDirty)
        }
        case None => NoTagVersion(commitHash, isDirty) // no commits in tags map => no tags
      }
    } else
      TaggedVersion(headTags.last, isDirty)
  }
}
