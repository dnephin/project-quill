package models

import play.api.libs.json._


class StatementModel {
    
    val jsonTransformer = (
            (__ \  'version) ++
            (__ \  '_id)
        ).json.pick


}