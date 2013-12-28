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


case class LabelNotUniqueError() extends Exception
case class BadVersionError() extends Exception

/** Create a new Statement
  */
object StatementAddLogic {

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
            result <- StatementData.add(stmt.setEditorId(userId))
        } yield result
    }
}


/**
  *  Update a statement
  *
  */
object StatementUpdateLogic {

    // TODO: validate editor.id against previous editor.id
    // (couch might be doing
    def apply(stmt: Statement, userId: String): Future[String] = {
        StatementData.update(stmt.setEditorId(userId))
    }

}


