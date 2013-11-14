
package auth.controllers


import play.api.mvc.{Controller, Action, Session}
import securesocial.core._

// TODO: upstream to securesocial


object LoginController extends Controller {

    // TODO: async response using async user service
    /**
     * Logout a user, return 200 on success, 400 if the user is not logged in.
     *
     *
     */
    def logout = Action { implicit request =>
        val user = for {
            authenticator <- SecureSocial.authenticatorFromRequest;
            user <- UserService.find(authenticator.identityId)
        } yield {
            Authenticator.delete(authenticator.id)
            user
        }
        user match {
            case Some(u) => Ok
            case None => BadRequest
        }
    }

}
