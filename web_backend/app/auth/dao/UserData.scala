package auth.dao

import auth.models._
import play.api.libs.ws.WS
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

import securesocial.core.IdentityId

/**
  * Data access for User identities
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
   
    // TODO: helper  got getById
    def getById(id: String) = {
        WS.url(s"$url/$id").get().map {
             response =>
                // TODO: don't use get
                response.json.validate[User].get
        }
        // TODO: error handling
   }

    def idFromViewResponse(json: JsValue) = {
        // TODO: Use a Reads/Format
        // TODO: raise 404 on missing
        ((json \ "rows")(0) \ "_id").as[String]
    }

   def getByIdentityId(identityId: IdentityId) = {
        WS.url(identityIdViewUrl)
            .withQueryString(
                "key" -> Json.arr(identityId.providerId,
                                 identityId.userId).toString())
            .get().flatMap {
                response => getById(idFromViewResponse(response.json))
            }
   }

}
