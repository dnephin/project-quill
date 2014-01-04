###
 statement/label.js - design document for statement database
###


module.exports = ddoc =
    _id: '_design/label'
    views: {}
    updates: {}


#
# Validate the document
#
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    ### !code common/validation.js ###

    validate newDoc, 'type', 'string'
    return if newDoc.type != 'label'

    validate newDoc, 'editor', 'string'
