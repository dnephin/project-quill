


QuillApp.Feedback = DS.Model.extend
    position:           DS.attr('string')
    text:               DS.attr('string')
    position:           DS.attr('string')
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
            # TODO: rename in backend
            obj.text = obj.content
            obj


    serialize: (record, options) ->
        obj = @_super(record, options)
        obj.editor =
            bio: obj.editorBio
        obj.content = obj.text

        delete obj.editorBio
        delete obj.text
        obj

