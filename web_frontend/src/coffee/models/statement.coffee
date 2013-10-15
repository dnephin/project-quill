


QuillApp.Version = DS.Model.extend
    number:         DS.attr('string')
    date:           DS.attr('date')
    message:        DS.attr('string')


QuillApp.Statement = DS.Model.extend
    title:          DS.attr('string')
    #version:        DS.belongsTo('version')
    version:        DS.attr()
    problem:        DS.attr('string')
    summary:        DS.attr('string')
    editor:         DS.attr('string')
    fullDocument:   DS.attr('string')
    active:         DS.attr('boolean')


#QuillApp.Adapter.map 'statement',
#    version:    { embedded: 'always' }



QuillApp.Statement.FIXTURES = [
    {
       id: 1
       title: "The sky is blue"
       version:
            number: "0.1"
            date: "2013-01-01 10:13:13"
            message: "It happened"
       summary: "Because I see it as blue"
    },
    {
       id: 2
       title: "Scala is cool"
       version: "1.0"
       summary: "Functional languages are the way to go."
    }

]
