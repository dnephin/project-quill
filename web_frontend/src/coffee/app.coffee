
window.QuillApp = Ember.Application.create()

QuillApp.ApplicationAdapter = DS.RESTAdapter.extend
    namespace: '/api'
    # TODO: dev only
    host: 'http://localhost:8083'
