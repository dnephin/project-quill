/**
 * 
 */
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.statement.StatementModel


/**
 * Statement controller
 */
object Statement extends Controller {

  def get(id: String) = Action {
    Ok(Json.obj("status" -> "OK", "message" -> "what"))
  }
  
  def update(id: String) = Action {
    Ok(Json.obj("status" -> "OK", "message" -> "what"))
  }
  
  // TODO: APIAction
  def add = Action(parse.json) { request =>
      request.body.validate(StatementModel.read).map {
          statement => Ok(Json.obj())
      }.recoverTotal {
          e => BadRequest(JsError.toFlatJson(e))
      }
  }

}
