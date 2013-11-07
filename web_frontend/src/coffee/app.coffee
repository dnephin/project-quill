#
# Application wide configuration for Ember app and data
#
#

window.QuillApp = Ember.Application.create()


QuillApp.ApplicationSerializer = DS.RESTSerializer.extend
    # CouchDB ids are _id
    primaryKey: '_id'


QuillApp.ApplicationAdapter = DS.RESTAdapter.extend
    # API endpoints are served from /api
    namespace: 'api'
    # api uses name, not plural of name
    pathForType: (type) -> type


# TODO: upstream?
class QuillApp.Store extends DS.Store

    adapterFor: (type) ->
        super @modelFor(type)

    # TODO: test case!
    # Find a single element by adapter function
    findByFunc: (type, adapterFunc, args...) ->
        type = @modelFor(type)
        @adapterFor(type)[adapterFunc](this, type, args...)
        .then (payload) =>
            payload = @serializerFor(type)
                     .extractSingle(this, type, payload, null, 'find')
            @push('statement', payload)
        .fail (error) ->
            throw error
