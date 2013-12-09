package quill.models

import play.api.libs.json._
import org.joda.time.DateTime


case class Anchor(
    statementId: String,
    parentId: String,
    // TODO: stricter context?
    context: String
)

object Anchor {
    implicit val format = Json.format[Anchor]
}


case class Response(
    _id: Option[String],
    editor: Editor,
    content: String,
    date: Option[DateTime],
    anchor: Anchor,
    // TODO: use an enum of some kind for other states
    active: Boolean = true
)


object Response {
    implicit val format = Json.format[Response]
}
