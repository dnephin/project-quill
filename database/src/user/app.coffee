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
# Validate the document
#
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    ### !code common/validation.js ###

    validate newDoc, 'identityId.providerId',   'string'
    validate newDoc, 'identityId.userId',       'string'
