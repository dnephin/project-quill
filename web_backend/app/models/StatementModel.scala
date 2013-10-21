/**
 * Statement models
 */
package quill.models

import play.api.libs.json._
import org.joda.time.DateTime


case class LabelModel(
	_id: String,
	editor: String
)


case class VersionModel(
	major: Int,
	minor: Int,
	patch: Int,
	published: Boolean,
	date: Option[DateTime]
)

// TODO: move to another file
case class EditorModel(
	id: String,
	bio: String
)


// TODO: field for finalized
case class StatementModel(
    _id: String,
    label: String,
	version: VersionModel,
	editor: EditorModel,
    title: String,
    problem: String,
    summary: String,
    full: String
)


object VersionModel {
	implicit val format = Json.format[VersionModel]
}

object EditorModel {
    implicit val format = Json.format[EditorModel]
}

object StatementModel {
    implicit val read = Json.reads[StatementModel]
    implicit val write = Json.writes[StatementModel]
    implicit val format = Json.format[StatementModel]
}

object LabelModel {
    implicit val format = Json.format[LabelModel]    
}

