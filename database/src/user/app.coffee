#
# user/app.js - design document for user database
#


module.exports = ddoc =
    _id: '_design/app'
    views: {}
    updates: {}


#
# Find a user by their identity id
#
ddoc.views.identity_id =
    map: (doc) ->
        id = doc.identityId
        emit [id.providerId, id.userId], null


#
# Add a new document with a default id
#
ddoc.updates.add = (doc, req) ->
    if doc
        return [null, "document already exists"]

    doc     = JSON.parse(req.body)
    id      = doc.identityId
    doc._id = "#{id.providerId}-#{id.userId}"
    return [doc, "added"]


#
# Validate the document
#
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    ### !code common/validation.js ###

    validate newDoc, 'identityId.providerId',   'string'
    validate newDoc, 'identityId.userId',       'string'
