/**
  */
package quill.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.escalatesoft.subcut.inject.BindingModule
import com.escalatesoft.subcut.inject.Injectable

import auth.logic.UserPublicGetLogic
import auth.models.User
import auth.models.UserPublic
import components.ApiBodyParser
import components.ApiController
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import quill.logic.BadVersionError
import quill.logic.LabelNotUniqueError
import quill.logic.StatementLogic
import quill.models.Statement
import securesocial.core.SecureSocial


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