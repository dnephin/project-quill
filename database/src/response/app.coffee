#
# app.js design document for response database
#


# TODO: is there a better way to support testing here?
module = if window?
    window.user = {}
else
    module
module.exports = ddoc =
    _id: '_design/app'
    views: {}
    updates: {}
    lists: {}
    shows: {}


# TODO: document validation


# TODO: tests
ddoc.views.by_statement =
    map: (doc) ->
        emit(doc.anchor.statementId, null)

