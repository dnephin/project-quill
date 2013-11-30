package quill.components


import play.api.mvc.{ BodyParsers, BodyParser }


/**
  * Uses:
  * * BodyParser - [[play.api.mvc.BodyParsers.parser.json]]
  *
  */
class ApiBodyParser[T] with BodyParsers {

    def apply: BodyParser[T] = json.flatMap { jsValue =>
        jsValue.validate[T] match {
            case JsSuccess(item) => {
                // TODO: is there an easier helper?
                tolerantBodyParser[T](T.toString(), DEFAULT_MAX_TEXT_LENGTH, "Bad Request") {
                    (request, bytes) => item
                }
            }
            // TODO: json error message with validation error
            case e: JsError => error(BadRequest(e.toString))
        }
    }

}

