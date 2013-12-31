###
 Unit tests for feedback/_design/app
###

# TODO: find a way to use relative path
ddoc = require('/vagrant/dist/database/feedback/app.js')


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

            ddoc.views.identity_id.map(doc)
            expect(emit).toHaveBeenCalledWith("statement", null)
