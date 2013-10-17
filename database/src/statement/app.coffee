#
# app.js design document for statement database
#


# TODO: is there a better way to support testing here?
module = window ? module
module.exports = ddoc =
    _id: '_design/app'
    views: {}
    updates: {}
    lists: {}
    shows: {}


ddoc.views.current_published =
    map: (doc) ->
        return if ! doc.version.published

        # TODO: deal with duplication
        buildVersion = (version) ->
            # TODO: document and restrict each version part to 999
            version.major * 1000 * 1000 + version.minor * 1000 + version.patch

        emit(doc.label, [doc._id, buildVersion(doc.version)])

    # TODO: deal with duplication
    reduce: (keys, values, rereduce) ->
        max = (prev, next) ->
            if prev[1] > next[1] then prev else next
        values.reduce(max)


ddoc.views.current =
    map: (doc) ->
        # TODO: deal with duplication
        buildVersion = (version) ->
            version.major * 1000 * 1000 + version.minor * 1000 + version.patch
        emit(doc.label, [doc._id, buildVersion(doc.version)])
    reduce: (keys, values, rereduce) ->
        max = (prev, next) ->
            if prev[1] > next[1] then prev else next
        values.reduce(max)


# TODO: error codes and handling?
# Update a document to be published
ddoc.updates.publish = (doc, req) ->
    if !doc
        return [null, "document not found"]

    doc.version.published = true
    doc.version.date = new Date().toISOString()
    return [doc, "published"]


# Update a document to a new version or save an existing unpublished version
ddoc.updates.update = (doc, req) ->
    if !doc
        return [null, "document not found"]

    newDoc = JSON.parse(req.body)
    if doc.editor.id != newDoc.editor.id
        return [null, "editor does not match"]

    if !doc.version.published
        return [newDoc, "updated document"]
   
    # TODO: deal with duplication
    buildVersion = (version) ->
        version.major * 1000 * 1000 + version.minor * 1000 + version.patch
    
    if buildVersion(newDoc.version) <= buildVersion(doc.version)
        return [null, "version was not incremented"]

    newDoc._id = req.uuid
    newDoc.version.date = new Date().toISOString()
    return [newDoc, "new document version"]


# Validate the document
ddoc.validate_doc_update = (newDoc, oldDoc, userCtx, secObj) ->
    if !oldDoc
        return

    if oldDoc.version.published
        throw(forbidden: 'document is already published')

    if oldDoc.editor.id != newDoc.editor.id
        throw(forbidden: 'editor does not match')

