package components

import securesocial.core.SecureSocial
import play.api.mvc.{ BodyParsers, BodyParser }
import play.api.libs.json._
import play.api.mvc.{ Results, ActionBuilder, Request, SimpleResult }
import play.api.libs.iteratee.Done
import play.api.libs.iteratee.Input
import play.api.mvc.Controller
import scala.concurrent.Future


/**
  * Uses:
  * * BodyParser - [[play.api.mvc.BodyParsers.parser.json]]
  *
  */
object ApiBodyParser extends BodyParsers {

    def apply[T](key: String)(implicit rds: Reads[T]): BodyParser[T] = parse.json.flatMap {
        jsValue => (jsValue \ key).validate[T] match {
            case JsSuccess(item, path) => {
                BodyParser("api parser") {
                    request => Done(Right(item), Input.Empty)
                }
            }
            // TODO: json error message with validation error
            case e: JsError =>
                parse.error(Future.successful(Results.BadRequest(e.toString)))
        }
    }
}


trait ApiController extends Controller {

    // TODO: move to a namespace specific to JSON
    def jsonId(typeName: String, id: String) = {
        Json.obj(typeName -> Json.obj("_id" -> id))
    }
}