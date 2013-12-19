

QuillApp.FeedbackRoutes = ->
    @route      'new',
    @route      'view',     path: '/:id'


QuillApp.FeedbackNewRoute = Ember.Route.extend

    setupController: (controller, model) ->
        controller.set 'content', {}


QuillApp.FeedbackIndexRoute = Ember.Route.extend

    model: (params, transition) ->
        # TODO: this shouldn't be necessary
        statementId = transition.params.label || \
            transition.providedModels.statement.id
        @store.findQuery('feedback', statementId: statementId)

    renderTemplate: (controller, model) ->
        @render 'statement/index',
            outlet: 'main'
            controller: 'statement'


QuillApp.FeedbackNewController = Ember.ObjectController.extend

    actions:
        save: (event) -> ""


QuillApp.FeedbackIndexController = Ember.ArrayController.extend
    needs: ['statement']
    statement: Ember.computed.alias('controllers.statement')
