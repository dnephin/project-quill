/**
  * Models for a User and authentication
  *
  *
  */

package auth.models


import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
import securesocial.core.{Identity, IdentityId, PasswordInfo}
import securesocial.core.{OAuth2Info, OAuth1Info, AuthenticationMethod}


case class UserLink(
    name: String,
    url: String,
    description: String,
    date: DateTime
)

object UserLink {
    implicit val format = Json.format[UserLink]
}


// TODO: split into three documents?
case class User(
    _id: String,
    identityId: IdentityId,
    links: Seq[UserLink],
    firstName: String,
    lastName: String,
    fullName: String,
    email: Option[String],
    avatarUrl: Option[String],
    authMethod: AuthenticationMethod,
    oAuth1Info: Option[OAuth1Info] = None,
    oAuth2Info: Option[OAuth2Info] = None,
    passwordInfo: Option[PasswordInfo] = None
) extends Identity


object User {

    implicit val formatId: Format[IdentityId] = (
        (__ \ "userId").format[String] and
        (__ \ "providerId").format[String]
    )(IdentityId.apply, unlift(IdentityId.unapply))

    implicit val formatAuthMethod: Format[AuthenticationMethod] =
        (__ \ "method").format[String].inmap(
            (method: String) => AuthenticationMethod(method),
            (method: AuthenticationMethod) => method.method)

    implicit val formatPasswordInfo: Format[PasswordInfo] = (
        (__ \ "hasher").format[String] and
        (__ \ "password").format[String] and
        (__ \ "salt").format[Option[String]]
    )(PasswordInfo.apply, unlift(PasswordInfo.unapply))

    implicit val formatOAuth1Info: Format[OAuth1Info] = (
        (__ \ "token").format[String] and
        (__ \ "secret").format[String]
    )(OAuth1Info.apply, unlift(OAuth1Info.unapply))

    implicit val formatOAuth2Info: Format[OAuth2Info] = (
        (__ \ "accessToken").format[String] and
        (__ \ "tokenType").format[Option[String]] and
        (__ \ "expiresIn").format[Option[Int]] and
        (__ \ "refreshToken").format[Option[String]]
    )(OAuth2Info.apply, unlift(OAuth2Info.unapply))

    implicit val format = Json.format[User]

    def fromIdentity(i: Identity) = {
        User("",
             i.identityId,
             Seq(),
             i.firstName,
             i.lastName,
             i.fullName,
             i.email,
             i.avatarUrl,
             i.authMethod,
             i.oAuth1Info,
             i.oAuth2Info,
             i.passwordInfo)
    }
}



// TODO: combine with user to remove duplication
/**
 *  A user object that can be sent to other users. It does not contain any
 *  sensitive data.
 */
case class UserPublic(
    _id: String,
    links: Seq[UserLink],
    firstName: String,
    lastName: String,
    fullName: String,
    avatarUrl: Option[String]
    // TODO: include email?, maybe masked
)

object UserPublic {
    implicit val format = Json.format[UserPublic]

    def fromUser(user: User) = {
        UserPublic(
            user._id,
            user.links,
            user.firstName,
            user.lastName,
            user.fullName,
            user.avatarUrl)
    }
}