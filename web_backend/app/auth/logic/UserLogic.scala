package auth.logic

import securesocial.core.{Identity, IdentityId}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import auth.models.User
import auth.dao.UserData
import auth.dao.UserNotFoundError
import auth.models.UserPublic

/**
 * Save user logic.
 */
object UserSaveLogic {

    def createNewUser(identity: Identity): Future[User] = {
        // TODO: generate id from identity
        // TODO: handle id conflict
        val user = User.fromIdentity(identity)
        UserData.add(user).map {
            userId => user.copy(_id=userId)
        }
    }


    // TODO: handle duplicate identities on different users
    def apply(identity: Identity): Future[User] = {
        UserData.getByIdentityId(identity.identityId).map {
            user => user
        } recoverWith {
            case UserNotFoundError(_) => createNewUser(identity)
        }
    }

}


/**
 * Retrieve a UserPublic object.
 */
object UserPublicGetLogic {

    def apply(userId: String): Future[UserPublic] = {
        UserData.getById(userId).map(UserPublic.fromUser(_))
    }

}