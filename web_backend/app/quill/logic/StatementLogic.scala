package quill.logic

import quill.models.Statement
import quill.dao.LabelData
import scala.concurrent.Future
import play.api.libs.json.JsValue
import play.api.libs.concurrent.Execution.Implicits._
import quill.models.Label
import quill.dao.StatementData
import quill.models.Version
import quill.models.Editor
import auth.models.User


case class LabelNotUniqueError() extends Exception
case class BadVersionError() extends Exception

/**
  *  Create a new Statement
  */
class StatementAddLogic(val dao: StatementData) {

    val acceptedVersion = Set("1.0.0", "0.1.0")

    // TODO: add editor
    def addUniqueLabel(label: String) = {
        LabelData.add(Label(label, "me")).map {
            result => if (result) true else throw new LabelNotUniqueError()
        }
    }

    def isAcceptedVersion(version: Version) = {
        acceptedVersion contains version.value
    }

    // TODO: validate editor.id
    def apply(stmt: Statement, userId: String): Future[String] = {
        if (!isAcceptedVersion(stmt.version)) Future {
            throw new BadVersionError()
        }

        else for {
            _ <- addUniqueLabel(stmt.label)
            result <- dao.add(stmt.setEditorId(userId))
        } yield result
    }
}


/**
  *  Update a statement
  */
class StatementUpdateLogic(val dao: StatementData) {

    // TODO: validate editor.id against previous editor.id
    // (couch might be doing
    def apply(stmt: Statement, userId: String): Future[String] = {
        dao.update(stmt.setEditorId(userId))
    }

}


class StatementLogic(dao: StatementData) {

    def add = new StatementAddLogic(dao)

    def update = new StatementUpdateLogic(dao)

    def getPublished(label: String) = dao.getCurrentPublished(label)

    def getCurrent(label: String) = dao.getCurrent(label)

    def publish(id: String, userId: String) = dao.publish(id, userId)

}
