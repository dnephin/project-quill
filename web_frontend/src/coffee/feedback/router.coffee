

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
        @render 'feedback/new',
            into: 'statement.summary'
            outlet: 'feedback'


QuillApp.FeedbackIndexRoute = Ember.Route.extend

    renderTemplate: (controller, model) ->
        @render 'feedback/list',
            into: 'statement.summary'
            outlet: 'feedback'


QuillApp.FeedbackNewController = Ember.ObjectController.extend

    actions:
        save: (event) -> ""


QuillApp.FeedbackIndexController = Ember.ArrayController.extend
    needs: ['statement']
    statement: Ember.computed.alias('controllers.statement')
