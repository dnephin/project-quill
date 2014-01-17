package test.quill.logic

import org.mockito.Mockito.verify
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import quill.dao.StatementData
import quill.logic.StatementAddLogic
import quill.logic.StatementLogic
import quill.models.Version
import quill.dao.LabelData


class StatementAddLogicSpec extends FunSuite with MockitoSugar {

    def fixture = new {
        val statementDAO = mock[StatementData]
        val labelDAO     = mock[LabelData]
        val logic        = new StatementAddLogic(statementDAO, labelDAO)
    }

    def versionNum(major: Int, minor: Int, patch: Int) = {
        Version(major, minor, patch, false, None)
    }

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
        val labelDAO     = mock[LabelData]
        val logic        = new StatementLogic(statementDAO, labelDAO)
    }

    test("getPublished calls dao") {
        val f = fixture
        val label = "this-is-the-label"
        val statement = f.logic.getPublished(label)
        verify(f.statementDAO).getCurrentPublished(label)
    }


}