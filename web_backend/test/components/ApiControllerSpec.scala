package test.components

import org.scalatest.FunSuite
import components.ApiController
import play.api.libs.json.Json


object SampleController extends ApiController


class ApiControllerSpec extends FunSuite {

    test("jsonId returns correct Json.Obj") {
        assert(SampleController.jsonId("name", "the_id") ===
            Json.obj("name" -> Json.obj("id" -> "the_id")))
    }

}