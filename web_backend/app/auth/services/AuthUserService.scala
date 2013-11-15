
package auth.services


import scala.concurrent.Await
import scala.concurrent.duration._
import play.api.Application
import play.api.libs.concurrent.Execution.Implicits._
import securesocial.core.{Identity, IdentityId, UserServicePlugin}
import securesocial.core.providers.Token

import auth.dao.UserData
import auth.models.{User, UserIdentity}


class AuthUserService(application: Application)
                      extends UserServicePlugin(application) {

    /**
     * Finds a user that maches the specified id
     *
     * @param id the user id
     * @return an optional user
     */
    def find(id: IdentityId) = {
        val optUser = UserData.getByIdentityId(id).map {
            case Some(user) => user.identity match {
                // TODO: why is all this necessary ?
                case uid: Identity => Some(uid)
                case _ => None
            }
            case _ => None
        }

        // TODO: better way to do this, or at least move to config
        Await.result(optUser, 2 seconds)
    }

    /**
     * Saves the user.  This method gets called when a user logs in.
     * This is your chance to save the user information in your backing store.
     * @param user
     */
    def save(identity: Identity): Identity = {
        // TODO: handle duplicate identities on different users
        // TODO: move to user logic?
        // Get user by identity
        val optIdentity = UserData.getByIdentityId(identity.identityId).map {
            case Some(user) => {
                UserData.add(user)
                user.identity
            }
            case _ => {
                val userId = UserIdentity.fromIdentity(identity)
                UserData.add(User(None, Seq(), userId))
                userId
            }
        }

         // TODO: better way to do this, or at least move to config
        Await.result(optIdentity, 2 seconds)
    }

    /**
     * Finds a user by email and provider id.
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation.
     *
     * @param email - the user email
     * @param providerId - the provider id
     * @return
     */
    def findByEmailAndProvider(email: String, providerId: String):Option[Identity] = {
        None
    }

    /**
     * Saves a token.  This is needed for users that
     * are creating an account in the system instead of using one in a 3rd party system.
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     * @param token The token to save
     * @return A string with a uuid that will be embedded in the welcome email.
     */
    def save(token: Token) = {
        None
    }

    /**
     * Finds a token
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     * @param token the token id
     * @return
     */
    def findToken(token: String): Option[Token] = {
        None
    }

    /**
     * Deletes a token
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     * @param uuid the token id
     */
    def deleteToken(uuid: String) {
    }

    /**
     * Deletes all expired tokens
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     */
    def deleteExpiredTokens() {
    }
}
