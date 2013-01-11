import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import collection.JavaConverters._
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{IndexDiff, Constants, ObjectId}
import org.eclipse.jgit.treewalk.FileTreeIterator

object Version {
  def main(args: Array[String]) {
    val repoDir = new File("target/test-repo")
    println("version = " + getVersion(Git.open(repoDir)))
  }

  def getVersion(git: Git): String = {
    val repo = git.getRepository

    val headId: ObjectId = repo.resolve(Constants.HEAD)

    val tags = for {
      (shortTagName, tag) <- repo.getTags.asScala
      if tag.getObjectId == headId
      if shortTagName(0).isDigit
    } yield shortTagName

    val version = if (tags.isEmpty) {
      val (latesttag, tagdistance) = getLatestTagWithDistance(git)
      latesttag + '+' + tagdistance + '-' + headId.name.slice(0, 12)
    } else tags.last

    val isDirty = new IndexDiff(repo, Constants.HEAD, new FileTreeIterator(repo)).diff()

    if (isDirty) version + "+" + new SimpleDateFormat("yyyyMMdd").format(new Date())
    else version
  }

  def getLatestTagWithDistance(git: Git): (String, Int) = {
    ("abc", 1)
  }
}
