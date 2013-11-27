package auth.dao

import auth.models._
import play.api.libs.ws.WS
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

import securesocial.core.IdentityId

case class UserNotFoundException(msg: String) extends Exception

// TODO: move to couchdb client lib
case class Conflict() extends Exception


/** Data access for User identities
  */
object UserData {

    // TODO: config
    val url = "http://localhost:5984/user"

    val identityIdViewUrl = s"$url/_design/app/_view/identity_id"

    // TODO: error handling and logging
    // TODO: move to couch client lib
    def add(user: User): Future[String] = {
        WS.url(url).post(Json.toJson(user)).map {
            response => response.status match {
                case 201 => (response.json \ "id").as[String]
                case 409 => throw Conflict()
            }
        }
    }

    // TODO: error handling and logging
    // TODO: move to couch client lib
    def getById(id: String): Future[User] = {
        WS.url(s"$url/$id").get().map {
            response => response.status match {
                case 200 => response.json.validate[User].get
                case 404 => throw UserNotFoundException(id)
            }
        }
    }

    // TODO: move to couch client lib
    val viewId = ((__ \ "rows")(0) \ "id").read[String]

    def getByIdentityId(identityId: IdentityId): Future[User] = {
        val key = Json.arr(identityId.providerId, identityId.userId).toString()
        val request = WS.url(identityIdViewUrl).withQueryString("key" -> key)

        request.get().flatMap { response =>
            viewId.reads(response.json) match {
                case userId: JsSuccess[String] => getById(userId.get)
                case e: JsError =>
                    throw UserNotFoundException(identityId.toString())
            }
        }
    }

}
