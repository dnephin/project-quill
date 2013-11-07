

# TODO: prevent labels like 'new'
QuillApp.Router.map ->

    @resource 'statement', ->
        @route 'new'
        @route 'view',      path: ':label'
        @route 'viewFull',  path: ':label/view'
        @route 'edit',      path: ':label/edit'


QuillApp.StatementNewRoute = Ember.Route.extend

    setupController: (controller, model) ->
        controller.set 'content', {}


QuillApp.StatementEditRoute = Ember.Route.extend

    # TODO: custom url for unpublished
    model: (params) ->
        @store.findByFunc('statement', 'findForEdit', params.label)


QuillApp.StatementViewRoute = Ember.Route.extend

    # TODO: this does not query by id, use different find method
    model: (params) -> @store.find('statement', params.label)


QuillApp.StatementViewFullRoute = Ember.Route.extend

    # TODO: this does not query by id, use different find method
    model: (params) -> @store.find('statement', params.label)


QuillApp.StatementNewController = Ember.ObjectController.extend

    actions:
        save: (event) ->
            @store.createRecord('statement', @content)
            .save()
            .then (obj) =>
                # TODO: add id to model
                @transitionToRoute('statement.edit', statement)
            .fail (reason) ->
                # TODO: report errors
                console.log "Error: #{reason.responseText}"


QuillApp.StatementEditController = Ember.ObjectController.extend

    actions:
        save: (event) ->
            @get('model').save()
            .then (obj) =>
                # TODO: show visual "saved" message
                console.log "Saved"
            .fail (reason) ->
                # TODO: report errors
                console.log "Error: #{reason.responseText}"
            
        publish: (event) ->
            console.log "Publishing"
            @store.adapterFor('statement').publish(@content.id)
            .then (obj) =>
                @set('published', true)
                console.log "published"
            .fail (reason) ->
                console.log "Error: #{reason.responseText}"
