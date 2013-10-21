package quill.dao

import quill.models.LabelModel
import play.api.libs.ws.WS
import play.api.libs.json.Json

object LabelData {
    
    // TODO: config
    val url = "http://localhost:5984/label"
        
    def add(label: LabelModel) = {
         WS.url(url).post(Json.toJson(label))
    }
    
    def get(id: String) = {
        WS.url(s"${url}/${id}").get()
    }

}