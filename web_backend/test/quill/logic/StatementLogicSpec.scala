package test.quill.logic

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import quill.dao.StatementData
import quill.logic.StatementAddLogic
import quill.models.Version
import org.scalatest.matchers.ClassicMatchers
import org.mockito.Mockito._
import quill.logic.StatementLogic


class StatementAddLogicSpec extends FunSuite with MockitoSugar {

    def fixture = new {
        val statementDAO = mock[StatementData]
        val logic = new StatementAddLogic(statementDAO)
    }

    def versionNum(major: Int, minor: Int, patch: Int) = {
        Version(major, minor, patch, false, None)
    }

//    def buildStatement(version: Version) = {
//        Statement(
//                Some("abab3a3"),
//                "example-statement",
//                version,
//                Editor(Some("ad2dffa23"), "I worked on it"),
//                "Example Statement",
//                "It needs to change",
//                "...",
//                "Change it like this")
//    }

    test("isAcceptedVersion should accept 1.0.0") {
        assert(fixture.logic.isAcceptedVersion(versionNum(1, 0, 0)))
    }

    test("isAcceptedVersion should accept 0.1.0") {
        assert(fixture.logic.isAcceptedVersion(versionNum(0, 1, 0)))
    }

    test("isAcceptedVersion should reject other versions") {
        for (version <- List((0, 0, 1), (2, 0, 0), (0, 2, 0))) {
            assert(!fixture.logic.isAcceptedVersion(
                    (versionNum _).tupled(version)))
        }
    }
}


class StatementLogicSpec extends FunSuite with MockitoSugar {

    def fixture = new {
        val statementDAO = mock[StatementData]
        val logic = new StatementLogic(statementDAO)
    }

    test("getPublished calls dao") {
        val f = fixture
        val label = "this-is-the-label"
        val statement = f.logic.getPublished(label)
        verify(f.statementDAO).getCurrentPublished(label)
    }


}