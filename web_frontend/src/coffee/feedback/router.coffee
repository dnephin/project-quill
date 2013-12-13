

QuillApp.Router.map ->

    @resource 'feedback', ->
        @route 'new'
        @route 'view',      path: ':id'

