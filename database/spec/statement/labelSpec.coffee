###
 Unit tests for statement/_design/label
###

ddoc = require('../../statement/label.js')

describe "statement/label design document", ->

    it "_id is correct", ->
        expect(ddoc._id).toBe("_design/label")

    describe "document validation", ->
        newDoc = null

        beforeEach ->
            newDoc =
                editor: "editor joe"
                type: "label"

        it "succeeds when the document is valid", ->
            expect( -> ddoc.validate_doc_update(newDoc)).not.toThrow()

        it "fails when the document is missing type", ->
            delete newDoc.type
            expect( -> ddoc.validate_doc_update(newDoc)).toThrow()

        it "fails when the document is missing an editor", ->
            delete newDoc.editor
            expect( -> ddoc.validate_doc_update(newDoc)).toThrow()

        it "skips non-label documents", ->
            newDoc = type: 'other'
            expect( -> ddoc.validate_doc_update(newDoc)).not.toThrow()
