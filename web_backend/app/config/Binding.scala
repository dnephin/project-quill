package config

import com.escalatesoft.subcut.inject.NewBindingModule
import com.escalatesoft.subcut.inject.BindingId
import play.api.Play
import components.couch.CouchClientUrl
import quill.logic.StatementLogic
import quill.dao.StatementData
import quill.dao.LabelData




object Binding extends NewBindingModule (module => {
    import module._

    val config = Play.current.configuration.underlying


    bind [StatementLogic] toSingle {
        val url = CouchClientUrl(config.getString("couch.url"), "statement")
        new StatementLogic(new StatementData(url), new LabelData(url))
    }

})