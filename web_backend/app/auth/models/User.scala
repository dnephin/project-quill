/**
  * A model for a User
  *
  *
  */

package auth.models 


import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
import securesocial.core.{Identity, IdentityId, PasswordInfo}
import securesocial.core.{OAuth2Info, OAuth1Info, AuthenticationMethod}



case class User(
    _id: Option[String],
    links: Seq[UserLink],
    identity: UserIdentity
)

case class UserLink(
    name: String,
    url: String,
    description: String,
    date: DateTime
)

case class UserIdentity(
    identityId: IdentityId,
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


object UserLink {
    implicit val format = Json.format[UserLink]
}

object UserIdentity {

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

    implicit val format = Json.format[UserIdentity]
}

object User {
    implicit val format = Json.format[User]
}

