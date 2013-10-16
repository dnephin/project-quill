#
# Unit tests for statement/_design/app
#

describe "statement app design document", ->

    describe "buildVersion builds the correct version", ->
    
        it "builds with all parts", ->
            source =
                major: 2
                minor: 7
                patch: 3
            expect(buildVersion(source)).toBe(2007003)
