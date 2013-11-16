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
        StatementData.getCurrent(id).map {
            stmt => Ok(Json.obj("statement" -> Json.toJson(stmt)))
        }
    }

    def update(id: String) = SecuredAction(ajaxCall=true).async(parse.json) {
        request => {
            Logger.warn(request.body.toString())
            Json.fromJson[Statement](request.body \ "statement") match {
            // TODO: check editor id matches user session
                case JsSuccess(stmt, path) => StatementAddLogic(stmt).map {
                    // TODO: check LogicResponse
                    response => Ok(response.toString())
                } recover {
                    case LabelNotUniqueError() => BadRequest("bad label")
                    case BadVersionError() => BadRequest("bad version")
                }
                case JsError(e) => {
                    Logger.warn(e.toString())
                    // TODO: make a JSON body for error
                    Future { BadRequest(e.toString()) }
                }
            }
        }
    }
    
    /**
     *  Publish a statement.
     *
     */
    def publish(id: String) = SecuredAction(ajaxCall=true).async { implicit request =>
        // TODO: move to logic
        //StatementData.getById(id).flatMap {
        //    stmt => if (stmt.editor.id != 
        // TODO: check editor id matches user session
        StatementData.publish(id).map {
            stmt => Ok(Json.obj())
        }
    }

    def add = SecuredAction(ajaxCall=true).async(parse.json) { request => {
        // TODO: cleanup
        Logger.warn(request.body.toString())
        val response = for {
            stmt <- Json.fromJson[Statement](request.body \ "statement");
            // TODO: store username in Identity
            maybeUser <- UserData.getByIdentityId(request.user.identityId);
            user <- maybeUser;
            response <- StatementAddLogic(stmt.addEditorId(user.editor.id))
       // TODO: check LogicResponse
        } yield response
        response.map {
            Ok(response.toString())
        }

        //        } recover {
        //            case LabelNotUniqueError() => BadRequest("bad label")
        //            case BadVersionError() => BadRequest("bad version")
        //            case JsError(e) => {
        //            Logger.warn(e.toString())
                    // TODO: make a JSON body for error
        //            Future.successful(BadRequest(e.toString()))
        //            }
        }
    }
}
