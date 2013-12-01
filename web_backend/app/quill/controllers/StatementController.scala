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


// TODO: remove ajaxCall=true with an abstraction
/** Statement controller
  */
object StatementController extends Controller with SecureSocial {

    def getPublished(id: String) = UserAwareAction.async {
        // TODO: add user_can_edit if editor matches user
        StatementData.getCurrentPublished(id).map {
            stmt => Ok(Json.obj("statement" -> Json.toJson(stmt)))
        }
    }

    // TODO: DRY with param
    def getUnpublished(id: String) = SecuredAction(ajaxCall=true).async {
        // TODO: compare editor id to document
        StatementData.getCurrent(id).map {
            stmt => Ok(Json.obj("statement" -> Json.toJson(stmt)))
        }
    }

    def update(id: String) = SecuredAction(ajaxCall=true).async(
            ApiBodyParser[Statement]("statement")) {
        request => {
            val stmt = request.body

            val response = for {
                user <- UserData.getByIdentityId(request.user.identityId)
                success <- StatementUpdateLogic(stmt, user._id)
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
    def publish(id: String) = SecuredAction(ajaxCall=true).async { implicit request =>

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

            val response = for {
                user <- UserData.getByIdentityId(request.user.identityId)
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
