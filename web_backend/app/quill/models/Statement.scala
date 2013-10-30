/** Statement models
  */
package quill.models

import play.api.libs.json._
import org.joda.time.DateTime

case class Label(
    _id: String,
    editor: String)

case class Version(
    major: Int,
    minor: Int,
    patch: Int,
    published: Boolean,
    date: Option[DateTime]
    // TODO: add a message
) {
    
    def value = s"$major.$minor.$patch"
}

case class Editor(
    id: String,
    bio: String)

// TODO: field for finalized
case class Statement(
    _id: Option[String],
    label: String,
    version: Version,
    editor: Editor,
    title: String,
    problem: String,
    summary: String,
    full: String)
    
// TODO: limit length of some string fields

object Version {
    implicit val format = Json.format[Version]
}

object Editor {
    implicit val format = Json.format[Editor]
}

object Statement {
    implicit val format = Json.format[Statement]
}

object Label {
    implicit val format = Json.format[Label]
}

