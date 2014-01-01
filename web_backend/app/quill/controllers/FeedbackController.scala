package quill.controllers

import components.ApiBodyParser
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import quill.dao.FeedbackData
import quill.models.Feedback
import securesocial.core.SecureSocial
import play.api.mvc.Action
import auth.models.User
import quill.logic.FeedbackAddLogic
import components.ApiController


/**
 * REST API controller are statement responses
 */
object FeedbackController extends ApiController with SecureSocial {

    def add() = SecuredAction(ajaxCall=true).async(
            ApiBodyParser[Feedback]("feedback")) { request =>

        // TODO: move into composed action
        val user = request.user match {
            case user: User => user
        }

        FeedbackAddLogic(request.body, user._id).map { feedbackId =>
            Ok(jsonId("feedback", feedbackId))
        }
    }


    def list() = Action.async(parse.empty) { request =>
        // TODO: logic
        // TODO: raise on missing
        val statementLabel = request.getQueryString("statement").get
        FeedbackData.getByStatementId(statementLabel).map { feedback =>
            Ok(Json.obj("feedback" -> feedback))
        }
    }

    def view(id: String) = Action.async(parse.empty) { request =>
        FeedbackData.getById(id).map { feedback =>
            Ok(Json.obj("feedback" -> feedback))
        }
    }

}
