package quill.dao

import play.api.Logger

import quill.models.Statement
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Future
import play.api.libs.ws.Response
import play.api.libs.concurrent.Execution.Implicits._


case class UpdateFailedError(msg: String) extends Exception
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

    val updateUrl = s"$url/_design/app/_update/update"

    val addUrl = s"$url/_design/app/_upadte/add"

    // TODO: do this with a handler and update the id to a friendly id
    // and push label at the same time
    def add(stmt: Statement): Future[String] = {
        WS.url(addUrl).post(Json.toJson(stmt)).map {
            // TODO: dry with publish()
            response => response.status match {
                case 201 => {
                    Logger.info(response.body)
                    response.header("X-Couch-Id").getOrElse("Unknown")
                 }
                case _ => {
                    val msg = response.body
                    Logger.warn(msg)
                    throw UpdateFailedError(msg)
                }
            }
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
                // TODO: upgrade couch to get this header
                case 201 => response.header("X-Couch-Id").getOrElse("Unknown")
                case _ => {
                    val msg = response.body
                    Logger.warn(msg)
                    throw PublishFailedError(msg)
                }
            }
        }
    }

    def update(stmt: Statement): Future[String] = {
        val stmtJson = Json.toJson(stmt)
        Logger.warn(stmtJson.toString())
        // TODO: don't use get
        WS.url(s"$updateUrl/${stmt._id.get}").post(stmtJson).map {
            // TODO: dry with publish()
            response => response.status match {
                case 201 => {
                    Logger.info(response.body)
                    response.header("X-Couch-Id").getOrElse("Unknown")
                 }
                case _ => {
                    val msg = response.body
                    Logger.warn(msg)
                    throw UpdateFailedError(msg)
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
