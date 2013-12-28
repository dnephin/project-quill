package quill.logic

import quill.models.Feedback
import auth.models.User
import scala.concurrent.Future
import quill.dao.FeedbackData


/**
 * Add a new feedback to a response
 */
object FeedbackAddLogic {

    // TODO: validate position
    def apply(feedback: Feedback, userId: String): Future[String] = {
        FeedbackData.add(feedback.setEditorId(userId))
    }

}