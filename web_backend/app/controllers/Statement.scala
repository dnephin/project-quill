/**
  */
package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json.Json
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
object Statement extends Controller {

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
        request => {
            // TODO: handle bad request
            val stmt = Json.fromJson[StatementModel](request.body).get
            // TODO: handle error response
            StatementData.add(stmt).map {
                response => Ok 
            }
        }
    }
}
