

# TODO: remove or create transform?
QuillApp.Version = DS.Model.extend
    major:          DS.attr('number')
    minor:          DS.attr('number')
    patch:          DS.attr('number')
    date:           DS.attr('date')
    published:      DS.attr('boolean')


QuillApp.Statement = DS.Model.extend
    label:          DS.attr('string')
    title:          DS.attr('string')
    version:        DS.attr()
    problem:        DS.attr('string')
    summary:        DS.attr('string')
    editor:         DS.attr()
    full:           DS.attr('string')


