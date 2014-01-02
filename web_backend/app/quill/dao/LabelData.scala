package quill.dao

import quill.models.Label
import play.api.libs.ws.WS
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

/**
  * Data access for Label model
  */
object LabelData {
    
    // TODO: config
    val url = "http://localhost:5984/statement"
    
    // TODO: DRY boilerplate, better response
    def add(label: Label): Future[Boolean] = {
        WS.url(url).post(Json.toJson(label)).map {
            // TODO: log unexpected codes (not 409)
            response => response.status == 201
        }
    }
   
    // TODO: helper  got getById
    def getById(id: String) = {
        WS.url(s"$url/$id").get().map {
             response =>
                response.json.validate[Label]
        }
   }

}
