

QuillApp.Router.map ->

    @resource 'statement', ->
        @route 'new'
        @route 'view',      path: ':id'
        @route 'viewFull',  path: ':id/view'


QuillApp.StatementNewRoute = Ember.Route.extend

    setupController: (controller, model) ->
        controller.set 'content', {}


QuillApp.StatementViewRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.id)


QuillApp.StatementViewFullRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.id)



# TODO: move to controllers
QuillApp.StatementNewController = Ember.ObjectController.extend

    isEditMode: true

    actions:
        save: ->
            console.log "save #{@content}"
            console.log @content
            @toggleProperty 'isEditMode'

        publish: ->
            console.log "publish"
            @store.createRecord 'statement', @content
            @toggleProperty 'isEditMode'
