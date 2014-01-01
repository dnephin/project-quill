

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
        # TODO: why is this necessary? why does it fire twice?
        if transition.params? and transition.params.label?
            @store.find('feedback', statement: transition.params.label)


QuillApp.FeedbackNewController = Ember.ObjectController.extend
    needs: ['statement']
    statement: Ember.computed.alias('controllers.statement')

    actions:
        save: (event) ->
            @set 'content.anchor',
                statementLabel: @get 'statement.label'
                parentId:       @get 'statement.id'
                # TODO: include a highlighted context text
                context:     ""
            @store.createRecord('feedback', @content)
            .save()
            .then (obj) =>
                # TODO: add id to model, transition using model
                @transitionToRoute('feedback.view', @content)
            .fail (reason) ->
                # TODO: report errors
                console.log reason


QuillApp.FeedbackIndexController = Ember.ArrayController.extend
    needs: ['statement']
    statement: Ember.computed.alias('controllers.statement')
