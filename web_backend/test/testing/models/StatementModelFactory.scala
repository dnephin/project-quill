package test.testing.models

import quill.models.Statement
import quill.models.Version
import quill.models.Editor
import org.joda.time.DateTime

object StatementFactory {

    def buildVersion(major: Int = 4,
                     minor: Int = 2,
                     patch: Int = 10,
                     published: Boolean = true,
                     date: Option[DateTime] = None) = {
        Version(major, minor, patch, published, date)
    }

    def buildEditor(id: Option[String] = Some("user-example"),
                    bio: String = "the bio") = {
        Editor(id, bio)
    }

    def apply(label: String = "the-label",
              version: Version = buildVersion(),
              editor: Editor = buildEditor(),
              title: String = "This is the Title",
              problem: String = "This is the problem.",
              summary: String = "This is the summary.",
              full: String = "This is the full text.") = {
        val id = Some(s"${label}-${version.major}.${version.minor}.${version.patch}")
        Statement(id, label, version, editor, title, problem, summary, full)
    }

    /**
      * Build three statements with the same label, and increasing versions.
      * The last one is unpublished.
      */
    def buildWithHistory(label: String, versions: Seq[Version]) = {
        for (version <- versions) yield apply(label=label, version=version)
    }
}
