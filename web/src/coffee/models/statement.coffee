

QuillApp.Statement = DS.Model.extend
    title: DS.attr('string')
    version: DS.attr('string')
    summary: DS.attr('string')


QuillApp.Statement.FIXTURES = [
    {
       id: 1
       title: "The sky is blue"
       version: "0.1"
       summary: "Because I see it as blue"
    },
    {
       id: 2
       title: "Scala is cool"
       version: "1.0"
       summary: "Functional languages are the way to go."
    }

]
