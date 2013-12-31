#
# feedback/app.js - design document for feedback database
#

module.exports = ddoc =
    _id: '_design/app'
    views: {}
    updates: {}


#
# A view which returns all feedback for a statementId
#
ddoc.views.by_statement =
    map: (doc) ->
        emit(doc.anchor.statementId, null)


# TODO: userId view
# TODO: userId + statementId view
