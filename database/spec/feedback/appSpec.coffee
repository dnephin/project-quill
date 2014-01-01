###
 Unit tests for feedback/_design/app
###

ddoc = require('../../feedback/app.js')


describe "feedback app design document", ->

    it "_id is correct", ->
        expect(ddoc._id).toBe("_design/app")

    describe "by_statement view mapper", ->

        beforeEach ->
            GLOBAL.emit = jasmine.createSpy('emit')

        afterEach ->
            GLOBAL.emit = undefined

        it "emits a document by it's statementId", ->
            doc =
                anchor:
                    statementId: "statement"

            ddoc.views.by_statement.map(doc)
            expect(emit).toHaveBeenCalledWith("statement", null)


    describe "document validation", ->
        newDoc = null

        beforeEach ->
            newDoc =
                anchor:
                    statementId: "the statement id"

        it "succeeds when document is valid", ->
            expect( -> ddoc.validate_doc_update(newDoc)).not.toThrow()

        it "fails when statementId is missing", ->
            delete newDoc.anchor.statementId
            expect( -> ddoc.validate_doc_update(newDoc)).toThrow()
