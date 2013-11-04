

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
    editorId:       DS.attr('string')


QuillApp.StatementSerializer = DS.RESTSerializer.extend
    primaryKey: '_id'

    normalizeHash:
        statement: (obj) ->
            version = obj.version
            # TODO: this is duplicated in couchdb, maybe store it this way?
            obj.version = "#{version.major}.#{version.minor}.#{version.patch}"
            obj.published = version.published
            obj.date = version.date
            obj.editorBio = obj.editor.bio
            obj.editorId = obj.editor.id
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
        delete obj.editorId
        delete obj.published
        delete obj.date

        obj
