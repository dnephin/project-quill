#
# app/ design document for statement database
#

couchapp = require('couchapp')
path = require('path')

ddoc =
    _id: '_design/app'
    views: {}
    lists: {}
    shows: {}

module.exports = ddoc


###
buildVersion - build a version string by joining the version parts
###
buildVersion = (version) ->
    # TODO: document and restrict each version part to 999
    version.major * 1000 * 1000 + version.minor * 1000 + version.patch


###
maxVersion - return the document with the highest version
###
maxVersion = (values) ->
    max = (prev, next) ->
        if prev.version.value > next.version.value then prev else next
    values.reduce(max)


# TODO: this doesn't work because buildVersion is not in the map
ddoc.views.current_published =
    map: (doc) ->
        return if ! doc.version.published
        doc.version.value = buildVersion(doc.version)
        emit(doc.label, doc)
    reduce: (keys, values, rereduce) ->
        maxVersion(values)


ddoc.views.current =
    map: (doc) ->
        doc.version.value = buildVersion(doc.version)
        emit(doc.label, doc)
    reduce: (keys, values, rereduce) ->
        maxVersion(values)
