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
    
    def add(stmt: StatementModel): Future[Response] = {
        // TODO: check version
        // TODO: check _id is not present
        WS.url(url).post(Json.toJson(stmt))
    }
}