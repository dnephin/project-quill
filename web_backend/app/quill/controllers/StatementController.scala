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

/** Statement controller
  */
object StatementController extends Controller {

    def getPublished(id: String) = Action.async {
        StatementData.getCurrentPublished(id).map {
            stmt => Ok(Json.obj("statement" -> Json.toJson(stmt)))
        }
    }

    // TODO: DRY with param
    def getUnpublished(id: String) = Action.async {
        StatementData.getCurrent(id).map {
            stmt => Ok(Json.obj("statement" -> Json.toJson(stmt)))
        }
    }

    def update(id: String) = Action {
        Ok(Json.obj("status" -> "OK", "message" -> "what"))
    }

    def add = Action.async(parse.json) {
        request =>
            {
                Logger.warn(request.body.toString())
                Json.fromJson[Statement](request.body) match {
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
