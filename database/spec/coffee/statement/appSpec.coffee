#
# Unit tests for statement/_design/app
#

ddoc = window.statement.exports

describe "statement app design document", ->

    it "_id is correct", ->
        expect(ddoc._id).toBe("_design/app")

    describe "current_published view", ->

        beforeEach ->
            window.emit = jasmine.createSpy('emit')

        afterEach ->
            window.emit = null

        it "map emits nothing if not published", ->
            source =
                version:
                    published: false
            ddoc.views.current_published.map(source)
            expect(emit.calls.length).toBe(0)

        it "map emits with version if publised", ->
            source =
                version:
                    major: 2
                    minor: 7
                    patch: 3
                    published: true
                label: 'the-label'
                _id: 'the id'
            ddoc.views.current_published.map(source)
            expect(emit).toHaveBeenCalledWith(
                source.label, [source._id, 2007003])

        it "reduce returns document with max version", ->
            docs = [
                [1, 1000]
                [2, 1005000]
                [3, 2003001]
                [4, 2001000]
            ]
            result = ddoc.views.current_published.reduce(null, docs, null)
            expect(result).toEqual([3, 2003001])


    describe "publish action", ->

        it "fails when document is not found", ->
            result = ddoc.updates.publish(null, null)
            expect(result).toEqual([null, "document not found"])

        it "succeeds when not published and editor matches", ->
            source =
                editor: { id: "abe" }
                version: { published: false }
            req = body: '{"editor": "abe"}'
            result = ddoc.updates.publish(source, req)
            expect(result).toEqual([source, "published"])
            expect(source.version.date).toEqual(jasmine.any(String))


    describe "update action", ->

        it "fails when document is not found", ->
            result = ddoc.updates.update(null, null)
            expect(result).toEqual([null, "document not found"])

        it "fails if the editor does not match", ->
            source =
                editor: { id: "abe" }
                version: { published: false }
            req = body: '{"editor": "sam"}'
            result = ddoc.updates.update(source, req)
            expect(result).toEqual([null, "editor does not match"])

        it "succeeds if previous was not published", ->
            source =
                editor: { id: "abe" }
                version: { published: false }
            req = body: JSON.stringify( editor: { id: "abe" })

            result = ddoc.updates.update(source, req)
            expect(result).toEqual([JSON.parse(req.body), "updated document"])

        it "fails if the version is not incremented", ->
            source =
                editor: { id: "abe" }
                version: { published: true, major: 3, minor: 2, patch: 7 }
            req = body: JSON.stringify
                editor: { id: "abe" }
                version: { major: 3, minor: 2, patch: 5 }

            result = ddoc.updates.update(source, req)
            expect(result).toEqual([null, "version was not incremented"])

        it "succeeds if there is a new version", ->
            source =
                editor: { id: "abe" }
                version: { published: true, major: 3, minor: 2, patch: 7 }
            req =
                uuid: "ababababa"
                body: JSON.stringify
                    editor: { id: "abe" }
                    version: { major: 3, minor: 3, patch: 0 }
                    title: "The title"

            result = ddoc.updates.update(source, req)
            [newDoc, msg] = result
            expect(msg).toBe("new document version")
            expect(newDoc.version.date).toEqual(jasmine.any(String))
            expect(newDoc._id).toBe(req.uuid)
            expect(newDoc.title).toBe("The title")


    describe "validate document on update", ->
        oldDoc = newDoc = null

        beforeEach ->
            oldDoc =
                editor: { id: "abe" }
                version: { published: false }
            newDoc =
                editor: { id: "abe" }
                version: { published: false }

        it "succeeds when the document is new", ->
            expect(ddoc.validate_doc_update).not.toThrow()

        it "fails when document is already published", ->
            oldDoc.version.published = true
            expect( -> ddoc.validate_doc_update(newDoc, oldDoc)).toThrow()

        it "fails if the editor does not match", ->
            newDoc.editor.id = "sam"
            expect( -> ddoc.validate_doc_update(newDoc, oldDoc)).toThrow()

        it "succeeds when document is valid", ->
            expect( -> ddoc.validate_doc_update(newDoc, oldDoc)).not.toThrow()

