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

    def update(id: String) = SecuredAction(ajaxCall=true) {
        // TODO: check editor id matches user session
        Ok(Json.obj())
    }
    
    def publish(id: String) = SecuredAction(ajaxCall=true).async {
        // TODO: check editor id matches user session
        StatementData.publish(id).map {
            stmt => Ok(Json.obj())
        }
    }

    def add = SecuredAction(ajaxCall=true).async(parse.json) { request => {
            Logger.warn(request.body.toString())
            Json.fromJson[Statement](request.body \ "statement") match {
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
            // TODO: handle error response
        }
    }
}
