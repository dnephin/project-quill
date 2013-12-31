###
 statement/app.js - design document for statement database
###


module.exports = ddoc =
    _id: '_design/app'
    views: {}
    updates: {}


#
# A view of the current published statement for each label.
#
ddoc.views.current_published =
    map: (doc) ->
        return if ! doc.version.published

        ### !code statement/build-version-macro.js ###
        emit(doc.label, [doc._id, buildVersion(doc.version)])

    reduce: (_, values) ->
        ### !code statement/current-version-reduce.js ###
        maxVersion(values)

#
# A view of the current statement for each label, the document maybe be
# published or un-published.
#
ddoc.views.current =
    map: (doc) ->
        ### !code statement/build-version-macro.js ###
        emit(doc.label, [doc._id, buildVersion(doc.version)])

    reduce: (_, values) ->
        ### !code statement/current-version-reduce.js ###
        maxVersion(values)

#
# Update a document to be published
#
ddoc.updates.publish = (doc, req) ->
    # TODO: error codes and error handling?
    if !doc
        return [null, "document not found"]

    reqDoc = JSON.parse(req.body)
    if reqDoc.editorId != doc.editor.id
        return [null, "editor id does not match #{doc.editor.id}"]


    doc.version.published = true
    doc.version.date = new Date().toISOString()
    return [doc, "published"]

#
# Update a document to a new version or save an existing unpublished version
#
ddoc.updates.update = (doc, req) ->
    if !doc
        return [null, "document not found"]

    newDoc = JSON.parse(req.body)
    if doc.editor.id != newDoc.editor.id
        return [null, "editor does not match"]

    if !doc.version.published
        # TODO: is this safe?
        newDoc._rev = doc._rev
        return [newDoc, "updated document"]

    ### !code statement/build-version-macro.js ###

    buildVersionString = (version) ->
        "#{version.major}.#{version.minor}.#{version.patch}"

    if buildVersion(newDoc.version) <= buildVersion(doc.version)
        return [null, "version was not incremented"]

    newDoc._id = "#{doc.label}-#{buildVersionString(newDoc.version)}"
    newDoc.version.date = new Date().toISOString()
    newDoc.version.published = false
    return [newDoc, "new document version"]


# TODO: test this is called for update handlers as well
#
# Validate the document
#
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    if !oldDoc
        return

    if oldDoc.version.published
        throw(forbidden: 'document is already published')

    if oldDoc.editor.id != newDoc.editor.id
        throw(forbidden: 'editor does not match')

