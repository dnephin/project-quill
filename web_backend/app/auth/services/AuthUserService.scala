
package auth.services


import play.api.Application
import play.api.libs.concurrent.Execution.Implicits._
import securesocial.core.{Identity, IdentityId, UserServicePlugin}
import securesocial.core.providers.Token

import auth.dao.UserData


class AuthUserService(application: Application)
                      extends UserServicePlugin(application) {

    /**
     * Finds a user that maches the specified id
     *
     * @param id the user id
     * @return an optional user
     */
    def find(id: IdentityId) = {
        // TODO: don't use get
        UserData.getByIdentityId(id).value.get.toOption match {
            case Some(user) => user.identity match {
                // TODO: why is all this necessary ?
                case uid: Identity => Some(uid)
                case _ => None
            }
            case _ => None
        }
    }

    /**
     * Saves the user.  This method gets called when a user logs in.
     * This is your chance to save the user information in your backing store.
     * @param user
     */
    def save(identity: Identity): Identity = {
        // TODO: handle race condition when first get misses
        // TODO: move to user logic?
        // Get user by identity
        UserData.getByIdentityId(identity.identityId).map {
            user => UserData.add(user)
        }
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
