package quill.dao

import scala.concurrent.Future

import components.couch.CouchClient
import components.couch.CouchClientUrl
import play.api.libs.json.Json
import quill.models.Label


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
