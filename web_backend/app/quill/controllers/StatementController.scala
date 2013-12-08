/**
  */
package quill.controllers

import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.ActionBuilder
import play.api.mvc.Request
import play.api.mvc.SimpleResult
import play.api.libs.json.Reads
import quill.models.Statement
import quill.dao.StatementData
import quill.logic.StatementAddLogic
import quill.logic.LabelNotUniqueError
import quill.logic.BadVersionError
import securesocial.core.SecureSocial
import auth.dao.UserData
import quill.logic.StatementUpdateLogic
import auth.models.User
import components.ApiBodyParser
import auth.logic.UserPublicGetLogic
import auth.models.UserPublic


// TODO: remove ajaxCall=true with an abstraction
/** Statement controller
  */
object StatementController extends Controller with SecureSocial {

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
            stmt <- StatementData.getCurrentPublished(id)
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
    def getUnpublished(id: String) = SecuredAction(ajaxCall=true).async { request =>
        // TODO: move to action
        val user = request.user match {
            case u: User => u
        }

        for {
            stmt <- StatementData.getCurrent(id)
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
                success <- StatementUpdateLogic(stmt.copy(_id=Some(id)), user._id)
            } yield Ok(success.toString())

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

        // TODO: move to logic
        StatementData.publish(id, user._id).map {
            id => Ok(Json.obj("_id" -> id))
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
                success <- StatementAddLogic(stmt, user._id)
            } yield Ok(success.toString())

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
