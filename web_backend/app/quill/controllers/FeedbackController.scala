package quill.controllers

import components.ApiBodyParser
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import quill.dao.FeedbackData
import quill.models.Feedback
import securesocial.core.SecureSocial
import play.api.mvc.Action


/**
 * REST API controller are statement responses
 */
object FeedbackController extends Controller with SecureSocial {


    def add() = SecuredAction(ajaxCall=true).async(
            ApiBodyParser[Feedback]("response")) { request =>
        // TODO: logic
        // TODO: add editorId
        FeedbackData.add(request.body).map { responseId =>
            Ok(Json.obj("_id" -> responseId))
        }
    }


    def list() = Action.async(parse.empty) { request =>
        // TODO: logic
        // TODO: raise on missing
        val statementId = request.getQueryString("statementId").get
        FeedbackData.getByStatementId(statementId).map { feedback =>
            Ok(Json.obj("feedback" -> feedback))
        }
    }

}
