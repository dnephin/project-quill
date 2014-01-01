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

#
# Validate the document
#
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    ### !code common/validation.js ###

    validate newDoc, 'anchor.statementId', 'string'
