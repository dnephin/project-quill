

# TODO: support the nested structure used by the backend

QuillApp.Statement = DS.Model.extend
    label:          DS.attr('string')
    title:          DS.attr('string')
    problem:        DS.attr('string')
    summary:        DS.attr('string')
    full:           DS.attr('string')

    version:        DS.attr('string')
    published:      DS.attr('boolean')
    date:           DS.attr('date')

    editorBio:      DS.attr('string')

    # TODO: use belongsTo?
    user:           DS.belongsTo('user')


QuillApp.StatementSerializer = DS.RESTSerializer.extend
    primaryKey: '_id'

    normalizePayload: (type, payload) ->
        if payload.user?
            payload.user = [payload.user]
        payload

    normalizeHash:
        statement: (obj) ->
            version = obj.version
            # TODO: this is duplicated in couchdb, maybe store it this way?
            obj.version = "#{version.major}.#{version.minor}.#{version.patch}"
            obj.published = version.published
            obj.date = version.date
            obj.editorBio = obj.editor.bio
            obj.user = obj.editor.id
            obj
            

    serialize: (record, options) ->
        obj = @_super(record, options)
        [major, minor, patch] = (obj.version or "0.0.0").split('.')
        obj.version =
            major: parseInt major
            minor: parseInt minor
            patch: parseInt patch
            published: obj.published
        obj.editor =
            bio: obj.editorBio

        delete obj.editorBio
        delete obj.published
        delete obj.date
        obj


QuillApp.StatementAdapter = QuillApp.ApplicationAdapter.extend

    # TODO: fails, I think because no models were put into context
    findForEdit: (store, type, label) ->
        @ajax(@buildURL(type.typeKey, label) + "/edit", 'GET')

    publish: (id) ->
        @ajax(@buildURL('statement', id) + "/publish", 'POST')
