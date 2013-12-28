

package quill.dao


import scala.concurrent.Future
import auth.dao.Conflict
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.libs.ws.WS
import quill.models.Feedback
import play.Logger
import auth.dao.Conflict


case class UnexpectedResponseFormat(msg: String) extends Exception
case class FeedbackNotFound() extends Exception


// TODO: do without this type, or move to couch client
case class CouchViewWithDoc(doc: Feedback)

object CouchViewWithDoc {
    implicit val format = Json.format[CouchViewWithDoc]
}


/**
 * Data access for Responses
 */
object FeedbackData {

    // TODO: config
    val url = "http://localhost:5984/feedback"

    val statementViewUrl = s"$url/_design/app/_view/by_statement"

    // TODO: move to couch client lib
    val viewRows = (__ \ "rows").read[Seq[CouchViewWithDoc]]

    // TODO: error handling and logging
    // TODO: move to couch client lib
    def add(response: Feedback): Future[String] = {
        WS.url(url).post(Json.toJson(response)).map {
            response => response.status match {
                case 201 => (response.json \ "id").as[String]
                case 409 => throw Conflict()
            }
        }
    }

    def getByStatementId(stmtId: String): Future[Seq[Feedback]] = {
        val key = JsString(stmtId).toString()
        WS.url(statementViewUrl)
            .withQueryString("key" -> key, "include_docs" -> "true")
            .get().map { response =>
                viewRows.reads(response.json) match {
                    case JsSuccess(responses, _)    => responses.map(_.doc)
                    case e: JsError                 =>
                        throw UnexpectedResponseFormat(e.toString)
                }
            }
    }

    /**
      * Retrieve a Feedback by id.
      */
    // TODO: helper for getById
    def getById(id: String): Future[Feedback] = {
        WS.url(s"$url/$id").get().map {
            response => {
                val content = response.json
                Logger.warn(content.toString())
                // TODO: use format, and don't use get
                content.validate[Feedback] match {
                    case stmt: JsSuccess[Feedback] => stmt.get
                    case JsError(e) => {
                        Logger.warn(e.toString())
                        throw FeedbackNotFound()
                    }
                }
            }
        }
    }

}