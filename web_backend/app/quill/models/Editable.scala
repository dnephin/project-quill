package quill.models

import play.api.libs.json.Json


case class Editor(
    id: Option[String],
    bio: String
) {
    def setId(userId: String) = {
        copy(id=Some(userId))
    }
}


object Editor {
    implicit val format = Json.format[Editor]
}


/**
 * An editable model has an editor
 */
trait Editable {

    def editor: Editor

    def setEditorId(editorId: String): Editable

}