package test.models

import play.api.libs.json._
import org.scalatest._
import models.statement.{ StatementModel, VersionModel, EditorModel }
import org.joda.time.DateTime

class StatementSpec extends FunSuite {

	val source = """
		{
			"_id": "abab3a3",
			"version": {
				"major": 1,
				"minor": 2,
				"patch": 0,
				"active": true,
				"date": null
			},
			"link": "example-statement",
			"title": "Example Statement",
			"editor": {
				"user": "ad2dffa23",
				"bio": "I worked on it"
			},
			"problem": "It needs to change",
			"summary": "...",
			"full": "Change it like this"
		}
	"""

	test("StatementModel parsed from json") {
		val expected = StatementModel(
			"abab3a3",
			"example-statement",
			VersionModel(1, 2, 0, true, None),
			EditorModel("ad2dffa23", "I worked on it"),
			"Example Statement",
			"It needs to change",
			"...",
			"Change it like this")
		assert(Json.fromJson[StatementModel](Json.parse(source)).get === expected)
	}

	test("VersionModel parsed from json") {
		val date = new DateTime(2013, 1, 2, 20, 10, 5)
		val expected = VersionModel(1, 2, 3, false, Some(date))
		val source = Json.obj(
			"major" -> 1,
			"minor" -> 2,
			"patch" -> 3,
			"active" -> false,
			"date" -> date)
		assert(Json.fromJson[VersionModel](source).get === expected)
	}

	test("VersionModel parsed from json without date") {
		val date = new DateTime(2013, 1, 2, 20, 10, 5)
		val expected = VersionModel(1, 2, 3, false, None)
		val source = Json.obj(
			"major" -> 1,
			"minor" -> 2,
			"patch" -> 3,
			"active" -> false,
			"date" -> JsNull)
		assert(Json.fromJson[VersionModel](source).get === expected)
	}

}