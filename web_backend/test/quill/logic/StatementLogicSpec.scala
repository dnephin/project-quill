package test.quill.logic

import org.scalatest.FunSpec
import org.scalatest.concurrent._
import org.scalatest.matchers.ShouldMatchers._
import quill.models._
import quill.logic.StatementAddLogic

class StatementAddLogicSpec extends FunSpec {
    
    def buildStatement(version: Version) = {
        Statement(
                Some("abab3a3"),
                "example-statement",
                version,
                Editor(Some("ad2dffa23"), "I worked on it"),
                "Example Statement",
                "It needs to change",
                "...",
                "Change it like this")        
    }

    describe("Add statement logic") {
        describe("is accepted version") {
            it("should accept version 1.0.0") {
                assert(StatementAddLogic.isAcceptedVersion(
                        Version(1, 0, 0, false, None)))
            }
            
            it ("should accept version 0.1.0") {
                assert(StatementAddLogic.isAcceptedVersion(
                        Version(0, 1, 0, false, None)))
            }
            
            it ("should not accept version 0.0.1") {
                assert(StatementAddLogic.isAcceptedVersion(
                        Version(0, 0, 1, false, None)) === false)
            }
        }
    }
}
