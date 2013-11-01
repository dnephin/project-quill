

QuillApp.ObjectTransform = DS.Transform.extend

    deserialize: (obj) ->
        return obj

    serialize: (obj) ->
        return obj


QuillApp.Statement = DS.Model.extend
    label:          DS.attr('string')
    title:          DS.attr('string')
    version:        DS.attr('object')
    problem:        DS.attr('string')
    summary:        DS.attr('string')
    editor:         DS.attr('object')
    full:           DS.attr('string')

