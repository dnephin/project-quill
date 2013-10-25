package quill.dao

import quill.models.StatementModel
import play.api.libs.ws.WS
import play.api.libs.json.Json
import scala.concurrent.Future
import play.api.libs.ws.Response

/** Data access operations for the statement databases
  */
object StatementData {

    // TODO: config
    val url = "http://localhost:5984/statement"

    var currentPublishedView = s"$url/_design/app/_view/current_published"
    
    var currentView = s"$url/_design/app/_view/current"
    
    def add(stmt: StatementModel): Future[Response] = {
        // TODO: check version
        // TODO: check _id is not present
        WS.url(url).post(Json.toJson(stmt))
    }

    def getCurrentPublished(label: String) = {
        WS.url(currentPublishedView)
            .withQueryString("group" -> "true", "key" -> label)
            .get()
            .map {
                response =>
                    // TODO: extract response handling
                    // TODO: raise 404 on missing
                    val id = (response.json \ "rows")(1)(__ \ "value").as[String]
                    getById(id)
            }
    }

    /**
      * Retrieve a Statement by id.
      */
    def getById(id: String) = {
        WS.url(s"$url/$id").get().map {
            response =>
                response.json.validate[StatementModel]
        }
    }
}
