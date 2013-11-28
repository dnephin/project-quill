package quill.dao

import play.api.Logger

import quill.models.Statement
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Future
import play.api.libs.ws.Response
import play.api.libs.concurrent.Execution.Implicits._


case class PublishFailedError(msg: String) extends Exception
case class StatementNotFound() extends Exception


/** Data access operations for the statement databases
  */
object StatementData {

    // TODO: config
    val url = "http://localhost:5984/statement"

    var currentPublishedViewUrl =
        s"$url/_design/app/_view/current_published"

    var currentViewUrl = s"$url/_design/app/_view/current"

    var publishUrl = s"$url/_design/app/_update/publish"

    def add(stmt: Statement): Future[Boolean] = {
        WS.url(url).post(Json.toJson(stmt)).map {
            response => response.status == 201
        }
    }

    def idFromCurrentViewResponse(json: JsValue) = {
        // TODO: Use a Reads/Format
        // TODO: raise 404 on missing
        ((json \ "rows")(0) \ "value")(0).as[String]
    }

    def currentView(label: String, viewUrl: String) = {
        WS.url(viewUrl)
            .withQueryString("group" -> "true",
                             "key" -> JsString(label).toString())
            .get()
            .flatMap {
                response =>
                    getById(idFromCurrentViewResponse(response.json))
            }
    }

    def getCurrentPublished(label: String) = {
        currentView(label, currentPublishedViewUrl)
    }

    def getCurrent(label: String) = {
        currentView(label, currentViewUrl)
    }

    def publish(id: String, editorId: String): Future[String] = {
        WS.url(s"$publishUrl/$id").post(Json.obj("editorId" -> editorId)).map {
            // TODO: logging and error handling
            response => response.status match {
                case 201 => response.header("X-Couch-Id").getOrElse("Unknown")
                case _ => {
                    val msg = response.body
                    Logger.warn(msg)
                    throw PublishFailedError(msg)
                }
            }
        }
    }

    /**
      * Retrieve a Statement by id.
      */
    // TODO: helper for getById
    def getById(id: String): Future[Statement] = {
        WS.url(s"$url/$id").get().map {
            response => {
                val content = response.json
                Logger.warn(content.toString())
                // TODO: use format, and don't use get
                content.validate[Statement] match {
                    case stmt: JsSuccess[Statement] => stmt.get
                    case JsError(e) => {
                        Logger.warn(e.toString())
                        throw StatementNotFound()
                    }
                }
            }
        }
    }
}
