package quill.logic

import quill.models.Feedback
import auth.models.User
import scala.concurrent.Future
import quill.dao.FeedbackData


/**
 * Logic for performing actions on Feedback objects.
 */
class FeedbackLogic(dao: FeedbackData) {

    // TODO: validate position field
    def add(feedback: Feedback, userId: String): Future[String] = {
        dao.add(feedback.setEditorId(userId))
    }

    def getByStatementLabel(label: String): Future[Seq[Feedback]] = {
        dao.getByStatementLabel(label)
    }

    def getById(id: String): Future[Feedback] = dao.getById(id)

}