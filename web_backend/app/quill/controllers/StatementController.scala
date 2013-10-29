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
import quill.models.StatementModel
import quill.dao.StatementData

/** Statement controller
  */
object StatementController extends Controller {

    def getPublished(id: String) = Action.async {
        StatementData.getCurrentPublished(id).map {
            stmt => Ok(Json.toJson(stmt))
        }
    }

    // TODO: DRY with param
    def getUnpublished(id: String) = Action.async {
        StatementData.getCurrent(id).map {
            stmt => Ok(Json.toJson(stmt))
        }
    }

    def update(id: String) = Action {
        Ok(Json.obj("status" -> "OK", "message" -> "what"))
    }

    def add = Action.async(parse.json) {
        request =>
            {
                Logger.warn(request.body.toString())
                Json.fromJson[StatementModel](request.body) match {
                    case JsSuccess(stmt, path) => StatementData.add(stmt).map {
                        response => Ok(response.body.toString())
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
