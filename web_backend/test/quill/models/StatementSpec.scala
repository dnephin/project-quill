package test.models

import play.api.libs.json._
import org.scalatest._
import quill.models.{ Statement, Version, Editor }
import org.joda.time.DateTime

class StatementSpec extends FunSuite {

	val source = """
		{
			"_id": "abab3a3",
			"version": {
				"major": 1,
				"minor": 2,
				"patch": 0,
				"published": true,
				"date": null
			},
			"label": "example-statement",
			"title": "Example Statement",
			"editor": {
				"id": "ad2dffa23",
				"bio": "I worked on it"
			},
			"problem": "It needs to change",
			"summary": "...",
			"full": "Change it like this"
		}
	"""

	test("Statement parsed from json") {
		val expected = Statement(
			Some("abab3a3"),
			"example-statement",
			Version(1, 2, 0, true, None),
			Editor(Some("ad2dffa23"), "I worked on it"),
			"Example Statement",
			"It needs to change",
			"...",
			"Change it like this")
		Json.fromJson[Statement](Json.parse(source)).map {
			stmt => assert(stmt === expected)    
		} recover {
		    case JsError(errors) => throw new AssertionError(errors.toString())
		}
	}

	test("Version parsed from json") {
		val date = new DateTime(2013, 1, 2, 20, 10, 5)
		val expected = Version(1, 2, 3, true, Some(date))
		val source = Json.obj(
			"major" -> 1,
			"minor" -> 2,
			"patch" -> 3,
			"published" -> true,
			"date" -> date)
		Json.fromJson[Version](source).map {
			version => assert(version === expected)
		} recover {
		    case JsError(errors) => throw new AssertionError(errors.toString()) 
		}
	}

	test("Version parsed from json without date") {
		val date = new DateTime(2013, 1, 2, 20, 10, 5)
		val expected = Version(1, 2, 3, false, None)
		val source = Json.obj(
			"major" -> 1,
			"minor" -> 2,
			"patch" -> 3,
			"published" -> false,
			"date" -> JsNull)
		Json.fromJson[Version](source).map {
			version => assert(version === expected)
		} recover {
		    case JsError(errors) => throw new AssertionError(errors.toString()) 
		}
	}
	
	test("Version value is correct") {
	    assert(Version(3, 100, 5, false, None).value === "3.100.5")
	}

}
