package components

import securesocial.core.SecureSocial
import play.api.mvc.{ BodyParsers, BodyParser }
import play.api.libs.json._
import play.api.mvc.{ Results, ActionBuilder, Request, SimpleResult }
import play.api.libs.iteratee.Done
import play.api.libs.iteratee.Input
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

//
//
//trait ApiAuthController extends SecureSocial {
//
//    object SecuredApiAction extends SecuredActionBuilder[SecuredRequest[_]] {
//
//        def apply[A]() = new SecuredActionBuikder[A](true, None).async(parse.json)
//
//    }
//
//    object UserApiAction extends ActionBuilder[RequestWithUser] {
//
//    }
//
//




