###
 Unit tests for feedback/_design/app
###

ddoc = require('../../feedback/app.js')


describe "feedback app design document", ->

    it "_id is correct", ->
        expect(ddoc._id).toBe("_design/app")

    describe "views", ->

        beforeEach ->
            GLOBAL.emit = jasmine.createSpy('emit')

        afterEach ->
            GLOBAL.emit = undefined

        describe "by_statement view mapper", ->

            it "emits a document by its statementLabel", ->
                doc =
                    anchor:
                        statementLabel: "statement"

                ddoc.views.by_statement.map(doc)
                expect(emit).toHaveBeenCalledWith("statement", null)

        describe "by_editor view mapper", ->

            it "emits a document by its editorId", ->
                doc =
                    anchor:
                        statementLabel: "statement"
                    editor:
                        id: "editor"
                ddoc.views.by_editor.map(doc)
                expect(emit).toHaveBeenCalledWith(
                    ["editor", "statement"], null)


    describe "document validation", ->
        newDoc = null

        beforeEach ->
            newDoc =
                anchor:
                    statementLabel: "the statement id"
                editor:
                    id: "editor"

        it "succeeds when document is valid", ->
            expect( -> ddoc.validate_doc_update(newDoc)).not.toThrow()

        it "fails when statementLabel is missing", ->
            delete newDoc.anchor.statementLabel
            expect( -> ddoc.validate_doc_update(newDoc)).toThrow()

        it "fails when editorId is missing", ->
            delete newDoc.editor
            expect( -> ddoc.validate_doc_update(newDoc)).toThrow()
