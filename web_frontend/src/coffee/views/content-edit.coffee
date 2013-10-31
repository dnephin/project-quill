
# TODO: contact and upstream
# modified from https://github.com/KasperTidemann/ember-contenteditable-view

# TODO: cleanup
# TODO: tests
#
# A view that is editable and displays a placeholder when empty
#
Ember.EditableView = Em.View.extend
    tagName: 'div'
    classNameBindings: ['isPlaceholder:placeholder']

    attributeBindings: ['contenteditable']
    contenteditable: 'true'

    hasFocus: false
    isUserTyping: false

    # Override placeholder in the template
    placeholder: 'default text'

    # Update content when value changes
    valueObserver: ( ->
        @setContent() if not @get('isUserTyping')
    ).observes('value')

    isPlaceholder: ( ->
        not @get('value') and not @get('hasFocus')
    ).property 'value', 'hasFocus'

    didInsertElement: ->
        # TODO: is there a better way to default this?
        # Default value to something defined
        @set 'value', @get('value') or ''
        @setContent()

    focusOut: ->
        @set 'hasFocus', false
        @set 'isUserTyping', false
        @setContent() if not @get('value')

    # Clear placeholder on focus
    focusIn: (event) ->
        @$().text '' if @get 'isPlaceholder'
        @set 'hasFocus', true

    keyDown: (event) ->
        @set 'isUserTyping', true if not event.metaKey

    keyUp: (event) ->
        @set 'value', @$().text()

    setContent: ->
        @$().text(@get('value') or @get('placeholder'))
