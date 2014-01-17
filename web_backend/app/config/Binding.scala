package config

import com.escalatesoft.subcut.inject.NewBindingModule

import components.couch.CouchClientUrl
import play.api.Play
import quill.dao.FeedbackData
import quill.dao.LabelData
import quill.dao.StatementData
import quill.logic.FeedbackLogic
import quill.logic.StatementLogic




object Binding extends NewBindingModule (module => {
    import module._

    val config = Play.current.configuration.underlying


    bind [StatementLogic] toSingle {
        val url = CouchClientUrl(config.getString("couch.url"), "statement")
        new StatementLogic(new StatementData(url), new LabelData(url))
    }

    bind [FeedbackLogic] toSingle {
        val url = CouchClientUrl(config.getString("couch.url"), "feedback")
        new FeedbackLogic(new FeedbackData(url))
    }

})