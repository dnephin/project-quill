package test.auth.models

import play.api.libs.json._
import org.scalatest._
import auth.models._
import org.joda.time.DateTime
import securesocial.core.{Identity, IdentityId, PasswordInfo}
import securesocial.core.{OAuth2Info, OAuth1Info, AuthenticationMethod}


class UserSpec extends FunSuite {

	val source = """
		{
			"_id": "profile_name",
            "links": [
                {
                    "name": "facebook",
                    "url": "http://facebook.com/someone",
                    "description": "Personal facebook account",
                    "date": 1384484278000 
                }
             ],
            "identity": {
                "identityId": {
                    "userId": "someone",
                    "providerId": "github"
                },
                "firstName": "Some",
                "lastName": "One",
                "fullName": "",
                "email": "someone@example.com",
                "avatarUrl": "http://avatarurl.example.com",
                "authMethod": {"method": "oauth2"},
                "oAuth1Info": null,
                "oAuth2Info": null,
                "passwordInfo": {
                    "hasher": "hasher",
                    "password": "password",
                    "salt": "the salt"
                }
            }
		}
	"""

	test("User parsed from json") {
		val expected = User(
			Some("profile_name"),
            Seq(UserLink("facebook",
                         "http://facebook.com/someone",
                         "Personal facebook account",
                         new DateTime(2013, 11, 15, 2, 57, 58))),
            UserIdentity(IdentityId("someone", "github"),
                         "Some",
                         "One",
                         "",
                         Some("someone@example.com"),
                         Some("http://avatarurl.example.com"),
                         AuthenticationMethod("oauth2"),
                         None,
                         None,
                         Some(PasswordInfo("hasher", "password", Some("the salt")))
            )
        )
		Json.fromJson[User](Json.parse(source)).map {
			user => assert(user === expected)
		} recover {
		    case JsError(errors) => throw new AssertionError(errors.toString())
		}
	}
}
