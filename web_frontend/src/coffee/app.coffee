
window.QuillApp = Ember.Application.create()


QuillApp.ApplicationSerializer = DS.RESTSerializer.extend
    primaryKey: '_id'


QuillApp.ApplicationAdapter = DS.RESTAdapter.extend
    namespace: 'api'
    pathForType: (type) -> type

