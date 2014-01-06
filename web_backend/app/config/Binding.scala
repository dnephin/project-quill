package config

import com.escalatesoft.subcut.inject.NewBindingModule
import com.escalatesoft.subcut.inject.BindingId
import play.api.Play
import components.couch.CouchClientConfig
import quill.logic.StatementLogic
import quill.dao.StatementData




object Binding extends NewBindingModule (module => {
    import module._

    val config = Play.current.configuration.underlying


    bind [StatementLogic] toSingle {
        new StatementLogic(
            new StatementData(
                CouchClientConfig(config.getString("couch.url"), "statement")))
    }

})