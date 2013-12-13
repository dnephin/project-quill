

package quill.dao


import scala.concurrent.Future

import auth.dao.Conflict
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.libs.ws.WS
import quill.models.Feedback


case class UnexpectedResponseFormat(msg: String) extends Exception


/**
 * Data access for Responses
 */
object FeedbackData {

    // TODO: config
    val url = "http://localhost:5984/user"

    val statementViewUrl = s"$url/_design/app/_view/by_statement"

    // TODO: move to couch client lib
    val viewRows = (__ \ "rows").read[Seq[Feedback]]

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
                    // TOOD: better way to do this?
                    case responses: JsSuccess[Seq[Feedback]] => responses.get
                    case e: JsError =>
                        throw UnexpectedResponseFormat(e.toString)
                }
            }
    }

}