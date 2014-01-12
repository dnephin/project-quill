
package auth.services

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import auth.dao.UserData
import auth.logic.UserSaveLogic
import play.api.Application
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import securesocial.core.Identity
import securesocial.core.IdentityId
import securesocial.core.UserServicePlugin
import securesocial.core.providers.Token


class AuthUserService(application: Application)
                      extends UserServicePlugin(application) {

    /**
     * Finds a user that matches the specified id
     *
     * @param id the user id
     * @return an optional user
     */
    def find(id: IdentityId): Option[Identity] = {
        val optUser = UserData.getByIdentityId(id).map {
            // TODO: another way to downcast ?
            user => user match {
                case uid: Identity => Some(uid)
            }
        } recover {
            // TODO: log errors
            case _ => None
        }

        // TODO: better way to do this, or at least move to config
        Await.result(optUser, 2 seconds)
    }

    /**
     * Saves the user.  This method gets called when a user logs in.
     *
     * @param user
     */
    def save(identity: Identity): Identity = {

         // TODO: better way to do this, or at least move to config
        Await.result(UserSaveLogic(identity), 2 seconds)
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
