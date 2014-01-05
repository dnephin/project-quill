###
 Unit tests for user/_design/app
###

ddoc = require('../../user/app.js')


describe "user design document", ->

    it "_id is correct", ->
        expect(ddoc._id).toBe("_design/app")

    describe "identity_id view mapper", ->

        beforeEach ->
            GLOBAL.emit = jasmine.createSpy('emit')

        afterEach ->
            GLOBAL.emit = undefined

        it "emits a document by it's identity", ->
            doc =
                identityId:
                    providerId: "provider"
                    userId: "the_user"

            ddoc.views.identity_id.map(doc)
            expect(emit).toHaveBeenCalledWith(["provider", "the_user"], null)


    describe "add new user", ->

        it "creates a new document with an id", ->
            req =
                body: JSON.stringify
                    identityId:
                        providerId: "provider"
                        userId: "the_user"
            [doc, msg] = ddoc.updates.add(null, req)
            expect(msg).toBe("added")
            expect(doc._id).toBe("provider-the_user")
            expect(doc.identityId.userId).toBe("the_user")


    describe "document validation", ->
        newDoc = null

        beforeEach ->
            newDoc =
                identityId:
                    providerId: "provider"
                    userId: "user_id"

        it "succeeds when document is valid", ->
            expect(-> ddoc.validate_doc_update(newDoc)).not.toThrow()

        it "fails when identityId is incomplete", ->
            delete newDoc.identityId.userId
            expect(-> ddoc.validate_doc_update(newdoc)).toThrow()
