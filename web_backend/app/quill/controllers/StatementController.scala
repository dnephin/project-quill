/**
  */
package quill.controllers

import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.ActionBuilder
import play.api.mvc.Request
import play.api.mvc.SimpleResult
import play.api.libs.json.Reads
import quill.models.Statement
import quill.dao.StatementData
import quill.logic.StatementLogic
import quill.logic.LabelNotUniqueError
import quill.logic.BadVersionError
import securesocial.core.SecureSocial
import auth.dao.UserData
import quill.logic.StatementUpdateLogic
import auth.models.User
import components.ApiBodyParser
import auth.logic.UserPublicGetLogic
import auth.models.UserPublic
import components.ApiController
import com.escalatesoft.subcut.inject.Injectable
import com.escalatesoft.subcut.inject.BindingModule
import play.api.mvc.Controller


// TODO: remove ajaxCall=true with an abstraction
/** Statement controller
  */
class StatementController(implicit val bindingModule: BindingModule)
        extends ApiController
        with SecureSocial
        with Injectable {

    implicit val statementLogic = inject [StatementLogic]

    // TODO: where does this belong?
    def userMatches(user: Option[User], other: UserPublic): Boolean = {
        user.map(_._id == other._id).getOrElse(false)
    }

    def getPublished(id: String) = UserAwareAction.async { request =>
        // TODO: move to action
        val user = request.user.map { user =>
            user match {
                case u: User => u
            }
        }

        for {
            stmt <- statementLogic.getPublished(id)
            // TODO: remove get
            stmtUser <- UserPublicGetLogic(stmt.editor.id.get)
        } yield {
            Ok(Json.obj(
                "statement" -> Json.toJson(stmt),
                "user" -> Json.toJson(stmtUser),
                "meta" -> Json.obj(
                    "user_can_edit" -> userMatches(user, stmtUser))
            ))
        }
    }

    // TODO: DRY with getPublished
    def getForEdit(id: String) = SecuredAction(ajaxCall=true).async { request =>
        // TODO: move to action
        val user = request.user match {
            case u: User => u
        }

        for {
            stmt <- statementLogic.getCurrent(id)
            // TODO: remove get
            stmtUser <- UserPublicGetLogic(stmt.editor.id.get)
        } yield {
            if (stmt.editor.id.getOrElse(false) == user._id)
                Ok(Json.obj(
                    "statement" -> Json.toJson(stmt),
                    "user" -> Json.toJson(stmtUser)
                ))
            else {
                Logger.warn(s"$stmt $user")
                Forbidden("Editor does not match user")
            }
        }
    }

    def update(id: String) = SecuredAction(ajaxCall=true).async(
            ApiBodyParser[Statement]("statement")) {
        request => {
            val stmt = request.body
            // TODO: move to action
            val user = request.user match {
                case u: User => u
            }

            val response = for {
                statementId <- statementLogic.update(
                        stmt.copy(_id=Some(id)), user._id)
            } yield Ok(jsonId("statement", statementId))

            response recover {
                case BadVersionError() => BadRequest("bad version")
                case e: NoSuchElementException => {
                    Logger.warn(e.toString())
                    // TODO: make a JSON body for error
                    BadRequest(e.toString())
                }
            }
        }
    }

    /**
     *  Publish a statement.
     *
     */
    def publish(id: String) = SecuredAction(ajaxCall=true).async(
            parse.empty) { implicit request =>

        // TODO: move into composed action
        val user = request.user match {
            case user: User => user
        }

        Logger.warn(s"Saving $id with user $user._id")
        statementLogic.publish(id, user._id).map {
            statementId => Ok(jsonId("statement", statementId))
        }
    }

    def add = SecuredAction(ajaxCall=true).async(
            ApiBodyParser[Statement]("statement")) {
        request => {
            val stmt = request.body
            // TODO: move into composed action
            val user = request.user match {
                case user: User => user
            }

            val response = for {
                statementId <- statementLogic.add(stmt, user._id)
            } yield Ok(jsonId("statement", statementId))

            response recover {
                case LabelNotUniqueError() => BadRequest("bad label")
                case BadVersionError() => BadRequest("bad version")
                case e: NoSuchElementException => {
                    Logger.warn(e.toString())
                    // TODO: make a JSON body for error
                    BadRequest(e.toString())
                }
            }
        }
    }
}