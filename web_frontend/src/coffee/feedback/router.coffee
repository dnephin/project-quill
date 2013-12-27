

QuillApp.FeedbackRoutes = ->
    @route      'new',
    @route      'view',     path: '/:id'



QuillApp.FeedbackRoute = Ember.Route.extend

    renderTemplate: (controller, model) ->
        @render 'statement/summary',
            controller: 'statement'


QuillApp.FeedbackNewRoute = Ember.Route.extend

    setupController: (controller, model) ->
        controller.set 'content', {}

    renderTemplate: (controller, model) ->
        @render
            outlet: 'feedback'


QuillApp.FeedbackIndexRoute = Ember.Route.extend

    renderTemplate: (controller, model) ->
        @render 'feedback/list',
            outlet: 'feedback'

    model: (params, transition) ->
        @store.find('feedback', statementId: transition.params.label)


QuillApp.FeedbackNewController = Ember.ObjectController.extend
    needs: ['statement']
    statement: Ember.computed.alias('controllers.statement')

    actions:
        save: (event) ->
            @set 'content.anchor',
                statementId: @get 'statement.label'
                parentId:    @get 'statement.id'
                # TODO: include a highlighted context text
                context:     ""
            @store.createRecord('feedback', @content)
            .save()
            .then (obj) =>
                # TODO: add id to model, transition using model
                @transitionToRoute('feedback.view', @content)
            .fail (reason) ->
                # TODO: report errors
                console.log "Error: #{reason.responseText}"


QuillApp.FeedbackIndexController = Ember.ArrayController.extend
    needs: ['statement']
    statement: Ember.computed.alias('controllers.statement')
