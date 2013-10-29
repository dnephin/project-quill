package quill.dao

import play.api.Logger

import quill.models.Statement
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Future
import play.api.libs.ws.Response
import play.api.libs.concurrent.Execution.Implicits._



/** Data access operations for the statement databases
  */
object StatementData {

    // TODO: config
    val url = "http://localhost:5984/statement"

    var currentPublishedView = s"$url/_design/app/_view/current_published"
    
    var currentView = s"$url/_design/app/_view/current"
    
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
        currentView(label, currentPublishedView)
    }

    def getCurrent(label: String) = {
        currentView(label, currentView)
    }


    /**
      * Retrieve a Statement by id.
      */
    // TODO: helper for getById
    def getById(id: String) = {
        WS.url(s"$url/$id").get().map {
            response =>
                // TODO: use format, and don't use get
                response.json.validate[Statement].get
        }
    }
}
