package quill.controllers

import com.escalatesoft.subcut.inject.BindingModule
import com.escalatesoft.subcut.inject.Injectable

import auth.models.User
import components.ApiBodyParser
import components.ApiController
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import quill.logic.FeedbackLogic
import quill.models.Feedback
import securesocial.core.SecureSocial


/**
 * REST API controller are statement feedback
 */
class FeedbackController(implicit val bindingModule: BindingModule)
        extends ApiController
        with SecureSocial
        with Injectable {

    implicit val feedbackLogic = inject [FeedbackLogic]


    def add() = SecuredAction(ajaxCall=true).async(
            ApiBodyParser[Feedback]("feedback")) { request =>

        // TODO: move into composed action
        val user = request.user match {
            case user: User => user
        }

        feedbackLogic.add(request.body, user._id).map { feedbackId =>
            Ok(jsonId("feedback", feedbackId))
        }
    }


    def list() = Action.async(parse.empty) { request =>
        // TODO: raise on missing
        val statementLabel = request.getQueryString("statement").get
        feedbackLogic.getByStatementLabel(statementLabel).map { feedback =>
            Ok(Json.obj("feedback" -> feedback))
        }
    }

    def view(id: String) = Action.async(parse.empty) { request =>
        feedbackLogic.getById(id).map { feedback =>
            Ok(Json.obj("feedback" -> feedback))
        }
    }

}
