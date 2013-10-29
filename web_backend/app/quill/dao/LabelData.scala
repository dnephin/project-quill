package quill.dao

import quill.models.Label
import play.api.libs.ws.WS
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Data access for LabelModel
  */
object LabelData {
    
    // TODO: config
    val url = "http://localhost:5984/label"
        
    def add(label: Label) = {
        WS.url(url).post(Json.toJson(label)).map {
            response => response.json
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
