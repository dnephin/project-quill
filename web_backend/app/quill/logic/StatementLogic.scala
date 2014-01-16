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
import components.couch.Conflict


case class LabelNotUniqueError() extends Exception
case class BadVersionError() extends Exception

/**
  *  Create a new Statement
  */
class StatementAddLogic(val stmtDao: StatementData, val labelDao: LabelData) {

    val acceptedVersion = Set("1.0.0", "0.1.0")

    def addUniqueLabel(label: String, userId: String) = {
        // TODO: test case for failure
        labelDao.add(Label(label, userId)) recover {
            case c: Conflict => throw new LabelNotUniqueError()
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
            _ <- addUniqueLabel(stmt.label, userId)
            result <- stmtDao.add(stmt.setEditorId(userId))
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


class StatementLogic(val stmtDao: StatementData, val labelDao: LabelData) {

    def add = new StatementAddLogic(stmtDao, labelDao)

    def update = new StatementUpdateLogic(stmtDao)

    def getPublished(label: String) = stmtDao.getCurrentPublished(label)

    def getCurrent(label: String) = stmtDao.getCurrent(label)

    def publish(id: String, userId: String) = stmtDao.publish(id, userId)

}
