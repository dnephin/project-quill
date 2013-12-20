

# TODO: prevent labels like 'new'
QuillApp.Router.map ->

    @resource 'statementNew',   path: '/statement/new'
    @resource 'statement',      path: '/statement/:label', ->
        @route      'edit',         path: '/edit'
        @route      'view'
        @resource   'feedback', QuillApp.FeedbackRoutes


### Routes ###

QuillApp.StatementNewRoute = Ember.Route.extend
    templateName: 'statement/new'

    setupController: (controller, model) ->
        controller.set 'content', {}


QuillApp.StatementRoute = Ember.Route.extend

    model: (params) -> @store.find('statement', params.label)

    serialize: (model) -> label: model.get('label')


QuillApp.StatementIndexRoute = Ember.Route.extend

    beforeModel: -> @replaceWith('feedback')


QuillApp.StatementEditRoute = Ember.Route.extend

    # TODO: does not cache
    # TODO: why is label in transition instead of params
    # just because it's a route and not a resource?
    model: (params, transition) ->
        @store.findByFunc('statement', 'findForEdit', transition.params.label)

    serialize: (model) -> label: model.get('label')

QuillApp.StatementViewRoute = Ember.Route.extend

    # TODO: why can't I get the model fron StatementRoute?
    model: (_, transition) ->
        @store.find('statement', transition.params.label)

    # TODO: do i need all of these?
    serialize: (model) -> label: model.get('label')


### Controllers ###

QuillApp.StatementNewController = Ember.ObjectController.extend

    actions:
        save: (event) ->
            @store.createRecord('statement', @content)
            .save()
            .then (obj) =>
                # TODO: add id to model
                @transitionToRoute('statement.edit', @content)
            .fail (reason) ->
                # TODO: report errors
                console.log "Error: #{reason.responseText}"


QuillApp.StatementEditController = Ember.ObjectController.extend

    actions:
        save: (event) ->
            # TODO: save is undefined when redirected from /new
            @get('content').save()
            .then (obj) =>
                # TODO: show visual "saved" message
                console.log "Saved"
            .fail (reason) ->
                # TODO: report errors
                console.log "Error: #{reason.responseText}"
            
        publish: (event) ->
            console.log "Publishing"
            statement = @set('content')
            @store.adapterFor('statement').publish(statement.id)
            .then (obj) =>
                # TODO: update from response from server instead
                @set('published', true)
                console.log "published"
                @transitionToRoute('statement.viewFull', statement)
            .fail (reason) ->
                console.log "Error: #{reason.responseText}"
