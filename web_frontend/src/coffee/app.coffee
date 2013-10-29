
window.QuillApp = Ember.Application.create()

QuillApp.ApplicationAdapter = DS.RESTAdapter.extend
    namespace: 'api'
    pathForType: (type) -> type
