


QuillApp.Feedback = DS.Model.extend
    position:           DS.attr('string')
    fullText:           DS.attr('string')
    date:               DS.attr('date')
    active:             DS.attr()

    editorBio:          DS.attr('string')
    user:               DS.belongsTo('user')

    anchor:             DS.attr()


QuillApp.FeedbackSerializer = DS.RESTSerializer.extend
    primaryKey: '_id'

    normalizeHash:
        feedback: (obj) ->
            obj.editorBio = obj.editor.bio
            obj.user = obj.editor.id
            obj


    serialize: (record, options) ->
        obj = @_super(record, options)
        obj.editor =
            bio: obj.editorBio

        obj.active = obj.active || true
        delete obj.editorBio
        delete user
        obj

