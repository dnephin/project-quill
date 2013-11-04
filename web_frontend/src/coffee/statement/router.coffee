

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


QuillApp.StatementViewRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.label)


QuillApp.StatementViewFullRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.label)


QuillApp.StatementNewController = Ember.ObjectController.extend

    actions:
        save: (event) ->
            statement = @store.createRecord('statement', @content)
            statement.save().then( (obj) =>
                # TODO: add id to model
                @transitionToRoute('statement.edit', statement)
            , (reason) ->
                # TODO: report errors
                console.log "Error: #{reason.responseText}"
            )

QuillApp.StatementEditController = Ember.ObjectController.extend

    actions:
        save: (event) ->
            # TODO: should be an update
            statement = @store.createRecord('statement', @content)
            statement.save().then( (obj) =>
                # TODO: show visual "saved" message
                console.log "Saved"
            , (reason) ->
                # TODO: report errors
                console.log "Error: #{reason.responseText}"
            )

        publish: (event) ->
            console.log "Publishing"
            Ember.$.getJSON("/api/statement/#{@content.id}/publish")
            .then( (obj) =>
                @set('published', true)
                console.log "published"
            , (reason) ->
                console.log "Error: #{reason.responseText}"
            )
