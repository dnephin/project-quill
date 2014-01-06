package components.couch

import play.api.libs.json.JsValue
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import play.api.libs.json.Reads
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import play.api.libs.json.JsString

/**
 * Thrown when a document update conflicts with another document (409)
 */
case class Conflict() extends Exception

/**
 * Thrown when the server returns 500
 */
case class ServerError(msg: String) extends Exception

/**
 * Thrown when the server responses with an unexpected response code
 */
case class UnknownResponseCode(code: Int, msg: String) extends Exception

/**
 * Thrown when the server response with a NotFound (404)
 */
case class NotFound() extends Exception

/**
 * A CouchClientConfig consists of a url to the couchDB server and a database
 * name.
 */
case class CouchClientUrl(hostUrl: String, name: String) {

    def url: String = hostUrl + "/" + name

    def view(designDocName: String, viewName: String) = {
        s"${url}/_design/${designDocName}/_view/${viewName}"
    }

    def update(designDocName: String, updateName: String) = {
        s"${url}/_design/${designDocName}/_upadte/${updateName}"
    }

    def updateWithId(designDocName: String, updateName: String, id: String) = {
        s"${url}/_design/${designDocName}/_upadte/${updateName}/${id}"
    }

    def withId(id: String) = {
        s"${url}/${id}"
    }
}


object CouchClient {


    def update(url: String, body: JsValue): Future[String] = {
        WS.url(url).post(body).map { response =>
            response.status match {
                case 201 => response.header("X-Couch-Id").get
                case 500 => throw ServerError(response.body)
                case 409 => throw Conflict()
                case _ =>   throw UnknownResponseCode(response.status,
                                                      response.body)
            }
        }
    }

    def getById[T](url: String)(implicit rds: Reads[T]): Future[T] = {
        WS.url(url).get().map { response =>
            response.status match {
                case 200 => response.json.validate[T] match {
                    case JsSuccess(obj, path) => obj
                    // TODO: log validation error
                    case JsError(error) => throw ServerError(error.toString())
                }
                case 404 => throw NotFound()
                case _ =>   throw UnknownResponseCode(response.status,
                                                      response.body)
            }
        }
    }

    def getIdFromView(url: String, key: String): Future[String] = {

        def idFromViewResponse(json: JsValue) = {
            // TODO: raise 404 on missing
            ((json \ "rows")(0) \ "value")(0).as[String]
        }

        WS.url(url)
            .withQueryString("group" -> "true",
                             "key" -> JsString(key).toString())
            .get()
            .map { response =>
                 idFromViewResponse(response.json)
            }
    }

}