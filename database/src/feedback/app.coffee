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

#
# A view which returns all feedback from a user. The key also contains a
# statementLabel so the returns can be filtered to a single statement.
#
ddoc.views.by_editor =
    map: (doc) ->
        emit([doc.editor.id, doc.anchor.statementLabel], null)

#
# Validate the document
#
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    ### !code common/validation.js ###

    validate newDoc, 'anchor.statementLabel', 'string'
    validate newDoc, 'editor.id', 'string'
