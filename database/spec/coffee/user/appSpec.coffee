###
 Unit tests for user/_design/app
###

# TODO: find a way to use relative path
ddoc = require('/vagrant/dist/database/user/app.js')


describe "user app design document", ->

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
