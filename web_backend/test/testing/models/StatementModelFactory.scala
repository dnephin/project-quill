package test.testing.models

import quill.models.Statement
import quill.models.Version
import quill.models.Editor
import org.joda.time.DateTime

object StatementModelFactory {

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

    def apply(_id: Option[String] = Some("1234abcdef"),
              label: String = "the-label",
              version: Version = buildVersion(),
              editor: Editor = buildEditor(),
              title: String = "This is the Title",
              problem: String = "This is the problem.",
              summary: String = "This is the summary.",
              full: String = "This is the full text.") = {
        Statement(_id, label, version, editor, title, problem, summary, full)
    }

}