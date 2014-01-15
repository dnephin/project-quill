package test.quill.dao

import java.io.File
import java.io.PrintWriter
import java.net.ServerSocket
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.sys.process.Process
import scala.util.Random
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.Suite
import components.couch.CouchClientUrl
import play.api.libs.ws.WS
import scala.io.Source
import quill.dao.StatementData
import test.testing.models.StatementModelFactory


// TODO: move to another file
class CouchDBSandbox(path: String, port: Int) {

    val configFilename = s"${path}/couch.ini"

    val command = s"couchdb -n -a /etc/couchdb/default.ini -a ${configFilename}"

    val host = s"http://localhost:${port}"

    // TODO: move to a file
    val config = s"""
[couchdb]
database_dir = ${path}
view_index_dir = ${path}
uri_file = ${path}/couch.uri
uuid = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

[httpd]
port = ${port}
bind_address = 0.0.0.0

[ssl]
port = ${port + 1}

[log]
file = ${path}/couch.log
level = warn
"""

    var process: Option[Process] = None

    def writeConfig = {
        new PrintWriter(configFilename) {
            print(config)
            close()
        }
    }

    def start = {
        // TODO: send stdout to a buffer
        process = Some(Process(command).run())
        // TODO: wait on startup function
        Thread.sleep(1000)
    }

    def stop = process.map(_.destroy)

    def createDatabase(name: String) = {
        val url = CouchClientUrl(host, name)
        Await.result(WS.url(url.url).put(""), 2 seconds)
    }

    def uploadDesignDoc(filename: String, dbname: String) = {
        val source = Source.fromFile(filename).mkString
        val url = CouchClientUrl(host, dbname)
        val future = WS.url(url.withId("_bulk_docs"))
            .withHeaders("Content-Type" -> "application/json")
            .post(source)
        Await.result(future, 2 seconds)
    }
}


trait CouchDBSandboxFixture {

    var sandbox: Option[CouchDBSandbox] = None

    val dbname = randomName

    def getPort = {
        new ServerSocket(0) { close }.getLocalPort
    }

    // TODO: a library to do this?
    def createTempDir = {
        val name = s"tmp_${randomName}"
        new File(System.getProperty("java.io.tmpdir"), name) {
            mkdirs
            deleteOnExit
        }.getPath
    }

    def randomName = {
        val characters = "abcdefghijklmnopqrstuwxyz"
        (for (n <- 0 to 10)
            yield characters(Random.nextInt(characters.length))).mkString
    }

    def startSandbox(name: String) = {
        val filename = s"../dist/database/${name}/ddocs.json"
        sandbox = Some(new CouchDBSandbox(createTempDir, getPort) {
            writeConfig
            start
            createDatabase(dbname)
            uploadDesignDoc(filename, dbname)
        })
    }

    // TODO: remove temporary dir
    def stopSandbox = sandbox.map(_.stop)

    // TODO: don't use get
    def couchUrl = CouchClientUrl(sandbox.get.host, dbname)
}


// TODO: use Futures in scalatest 2.0
class StatementDataSpec extends FunSuite
                        with CouchDBSandboxFixture
                        with BeforeAndAfterAll {


    override def beforeAll() = startSandbox("statement")
    override def afterAll() = stopSandbox

    test("Adding a document returns the id on success") {
        val data = new StatementData(couchUrl)
        val future = data.add(StatementModelFactory(label="add-test"))
        val stmtId = Await.result(future, 2 seconds)
        assert(stmtId === "add-test-4.2.10")
    }

}