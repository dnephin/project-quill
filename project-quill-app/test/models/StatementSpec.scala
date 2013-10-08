package test.models

import play.api.libs.json._
import org.scalatest._


import models.StatementModel

class StatementSpec extends FunSuite {
    
    val source = """
	    {
	        "_id": "abab3a3",
	        "version": "4.4.4",
	        "link": "example-statement",
	        "title": "Example Statement"
	    }
    """
    
    test("transform validates a statement") {
        val expected = Json.obj(
        	"_id" -> "abab3a3",
        	"version" -> "4.4.4",
	        "link" -> "example-statement",
	        "title" -> "Example Statement"
        )
        assert(Json.parse(source).transform(new StatementModel().jsonTransformer).get === expected)
    }

}