package quill.dao

import scala.concurrent.Future

import components.couch.CouchClient
import components.couch.CouchClientUrl
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import quill.models.Statement


/**
  *  Data access operations for the statement databases
  */
class StatementData(url: CouchClientUrl) {

    def client = CouchClient

    /**
     * Add a statement and return the new _id
     */
    def add(statement: Statement): Future[String] = {
        client.update(url.update("app", "add"), Json.toJson(statement))
    }

    /**
     * Get the currently published statement for a label
     */
    def getCurrentPublished(label: String): Future[Statement] = {
        client.getIdFromView(url.view("app", "current_published"), label)
            .flatMap(getById(_))
    }

    def getCurrent(label: String): Future[Statement] = {
        client.getIdFromView(url.view("app", "current"), label)
            .flatMap(getById(_))
    }

    def publish(id: String, editorId: String): Future[String] = {
        client.update(url.updateWithId("app", "publish", id),
                      Json.obj("editorId" -> editorId))
    }


    def update(stmt: Statement): Future[String] = {
        client.update(url.updateWithId("app", "update", stmt.id),
                      Json.toJson(stmt))
    }

    def getById(id: String): Future[Statement] = {
        client.getById[Statement](url.withId(id))
    }
}