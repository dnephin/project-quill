

# TODO: prevent labels like 'new'
QuillApp.Router.map ->

    @resource 'statement', ->
        @route 'new'
        @route 'view',      path: ':label'
        @route 'viewFull',  path: ':label/view'


QuillApp.StatementNewRoute = Ember.Route.extend

    setupController: (controller, model) ->
        controller.set 'content', {}


QuillApp.StatementViewRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.label)


QuillApp.StatementViewFullRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.label)



# TODO: move to controllers
QuillApp.StatementNewController = Ember.ObjectController.extend

    actions:
        save: ->
            console.log "save #{@content}"

        publish: ->
            console.log "publish"
