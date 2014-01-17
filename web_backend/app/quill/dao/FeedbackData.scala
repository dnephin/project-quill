package quill.dao

import scala.concurrent.Future
import components.couch.CouchClient
import components.couch.CouchClientUrl
import play.api.libs.json.Json
import quill.models.Feedback
import play.api.libs.json.JsString


/**
 * Data access for Feedback to statements
 */
class  FeedbackData(url: CouchClientUrl) {

    def client = CouchClient

    def add(feedback: Feedback): Future[String] = {
        client.add(url.url, Json.toJson(feedback))
    }

    def getByStatementLabel(statementLabel: String): Future[Seq[Feedback]] = {
        val key = JsString(statementLabel).toString()
        client.getFromViewByKey[Feedback](url.view("app", "by_statement"), key)
    }

    /**
      * Get a Feedback by id
      */
    def getById(id: String): Future[Feedback] = {
        client.getById[Feedback](url.withId(id))
    }

}
