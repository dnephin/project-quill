package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

object Statement extends Controller {

  def get(id: String) = Action {
    Ok(Json.obj("status" -> "OK", "message" -> "what"))
  }
  
  def update(id: String) = Action {
    Ok(Json.obj("status" -> "OK", "message" -> "what"))
  }
  
  def add = Action {
    Ok(Json.obj("status" -> "OK", "message" -> "what"))
  }

}
