package auth.dao

import auth.models._
import play.api.libs.ws.WS
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

import securesocial.core.IdentityId

case class UserNotFoundException(msg: String) extends Exception

/** Data access for User identities
  */
object UserData {

    // TODO: config
    val url = "http://localhost:5984/user"

    val identityIdViewUrl = s"$url/_design/app/_view/identity_id"

    // TODO: DRY boilerplate, better response
    def add(user: User): Future[Boolean] = {
        WS.url(url).post(Json.toJson(user)).map {
            // TODO: log unexpected codes (not 409)
            // TODO: add _id to user
            response => response.status == 201
        }
    }

    // TODO: helper for getById
    def getById(id: String): Future[Option[User]] = {
        WS.url(s"$url/$id").get().map { response =>
            response.json.validate[User].asOpt
        }
        // TODO: error handling and logging
    }

    def idFromViewResponse(json: JsValue): Option[String] = {
        // TODO: Use a Reads/Format
        // TODO: raise 404 on missing
        ((json \ "rows")(0) \ "id").asOpt[String]
    }

    // TODO: store username in Identity
    def getByIdentityId(identityId: IdentityId): Future[Option[User]] = {
        val request = WS.url(identityIdViewUrl)
            .withQueryString("key" -> Json.arr(identityId.providerId,
                identityId.userId).toString())

        request.get().flatMap { response =>
            idFromViewResponse(response.json) match {
                case Some(userId) => getById(userId)
                case None         => Future.successful(None)
            }
        }
    }

}
