#
# feedback/app.js - design document for feedback database
#

module.exports = ddoc =
    _id: '_design/app'
    views: {}
    updates: {}


#
# A view which returns all feedback for a statementLabel
#
ddoc.views.by_statement =
    map: (doc) ->
        emit(doc.anchor.statementLabel, null)


# TODO: userId view
# TODO: userId + statementLabel view

#
# Validate the document
#
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    ### !code common/validation.js ###

    validate newDoc, 'anchor.statementLabel', 'string'
