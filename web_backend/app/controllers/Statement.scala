/**
 * 
 */
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._

import models.statement.StatementModel
import dao.statement.StatementData


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
  def add = Action.async {
      request => {
          // TODO: handle validation error, non-json request
		  val statement = Json.fromJson[StatementModel](request.body.asJson.get).get
		  // TODO: check id/user/version(active)
		  StatementData.save(statement).map {
		      // TODO: handle errors
		      response => Ok(response.json)
		  }
      }
  }

}
