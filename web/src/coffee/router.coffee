

QuillApp.Router.map ->

    @resource 'home',           path: '/'
    @resource 'statement',      path: '/statement/:id'


QuillApp.StatementRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.id)

