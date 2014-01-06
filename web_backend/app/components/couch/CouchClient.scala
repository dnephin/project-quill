package components.couch


/**
 * A CouchClientConfig consists of a url to the couchDB server and a database
 * name.
 */
case class CouchClientConfig(url: String, name: String)


trait CouchClient {

}