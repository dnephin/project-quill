package auth.dao

import scala.concurrent.Future

import auth.models.User
import components.couch.CouchClient
import components.couch.CouchClientUrl
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import securesocial.core.IdentityId


case class UserNotFoundError(msg: String) extends Exception(msg)


// TODO: make this a class once controller supports DI
/**
 * Data access for User identities
 */
object UserData {

    val url = new CouchClientUrl("http://localhost:5984", "user")

    def client = CouchClient

    val identityIdViewUrl = s"$url/_design/app/_view/identity_id"

    val addUserUrl = s"$url/_design/app/_update/add"

    def add(user: User): Future[String] = {
        client.update(url.update("app", "add"), Json.toJson(user))
    }

    def getById(id: String): Future[User] = {
        client.getById[User](url.withId(id))
    }

    def getByIdentityId(identityId: IdentityId): Future[User] = {
        val key = Json.arr(identityId.providerId, identityId.userId).toString()
        client.getFromViewByKey[User](url.view("app", "identity_id"), key)
            .map { users =>
                users match {
                    case Seq(user)  => user
                    case Seq()      => throw new UserNotFoundError(key)
                }
        }
    }

}
