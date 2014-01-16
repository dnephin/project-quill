package quill.dao

import quill.models.Label
import play.api.libs.ws.WS
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import components.couch.CouchClientUrl
import components.couch.CouchClient

/**
  * Data access for Label model
  */
class LabelData(url: CouchClientUrl) {
    
    def client = CouchClient

    // TODO add type = label or a view
    /**
     * Add a new unique label
     */
    def add(label: Label): Future[String] = {
        client.add(url.url, Json.toJson(label))
    }
   
    /**
     * Get a label by Id
     */
    def getById(id: String) = {
        client.getById[Label](url.withId(id))
   }

}
