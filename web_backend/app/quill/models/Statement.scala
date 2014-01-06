/** Statement models
  */
package quill.models

import play.api.libs.json._
import org.joda.time.DateTime


case class Label(
    _id: String,
    editor: String
)


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


// TODO: require non-empty
// TODO: limit length of some string fields
// TODO: field for finalized
case class Statement(
    _id: Option[String],
    label: String,
    version: Version,
    editor: Editor,
    title: String,
    problem: String,
    summary: String,
    full: String
) extends Editable {

    def id = _id.get

    def setEditorId(editorId: String) = {
        copy(editor=editor.setId(editorId))
    }
}


object Version {
    // TODO: should this be moved into config/globals
    val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    implicit val dateTimeReads = Reads.jodaDateReads(dateFormat)
    implicit val dateTimeWrites = Writes.jodaDateWrites(dateFormat)

    implicit val format = Json.format[Version]
}


object Statement {
    implicit val format = Json.format[Statement]
}


object Label {
    implicit val format = Json.format[Label]
}

