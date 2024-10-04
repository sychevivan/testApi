import com.example.example.AddObject
import com.example.example.AddObjectRequest
import com.example.example.AddObjectRequestData
import com.example.example.ListOfObjectsByIDs
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.*
import io.ktor.client.plugins.contentnegotiation.*

class ApiTest {

    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    @Test
    fun testGetObjectsByIdsSuccess() = runBlocking {
        val url = "https://api.restful-api.dev/objects?id=3&id=5&id=10"

        val response: List<ListOfObjectsByIDs> = client.get(url).body()

        assertEquals(3, response.size)
        assertEquals("3", response[0].id)
        assertEquals("Apple iPhone 12 Pro Max", response[0].name)
        assertEquals("5", response[1].id)
        assertEquals("Samsung Galaxy Z Fold2", response[1].name)
        assertEquals("10", response[2].id)
        assertEquals("Apple iPad Mini 5th Gen", response[2].name)
    }

    @Test
    fun testGetObjectsByIdsFailure() = runBlocking {

        val url = "https://api.restful-api.dev/objects?id=9999&id=q"
        val response: List<ListOfObjectsByIDs> = client.get(url).body()

        assertTrue(response.isEmpty(), "Expected empty response but got: $response")
    }


    @Test
    fun testAddObjectSuccess(): Unit = runBlocking {
        val url = "https://api.restful-api.dev/objects"

        val requestData = AddObjectRequest(
            name = "Apple MacBook Pro 16",
            data = AddObjectRequestData(
                year = 2019,
                price = 1849.99,
                CPUModel = "Intel Core i9",
                HardDiskSize = "1 TB"
            )
        )

        val response: AddObject = client.post(url) {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(requestData)
        }.body()

        assertNotNull(response.id, "ID should not be null")
        assertTrue(response.id!!.length >= 16, "ID should be at least 16 characters long")
        assertTrue(response.id!!.matches(Regex("[a-fA-F0-9]+")), "ID should contain only hexadecimal characters")

        assertEquals("Apple MacBook Pro 16", response.name)
        assertNotNull(response.createdAt, "createdAt field should not be null")
    }


    @Test
    fun testAddObjectFailure(): Unit = runBlocking {
        val url = "https://api.restful-api.dev/objects"

            val response = client.post(url) {

            }

        val responseBody: String = response.body()
        assertTrue(responseBody.contains("\"error\""), "Expected 'error' field in the response, but got: $responseBody")
        assertEquals("{\"error\":\"400 Bad Request. If you are trying to create or update the data, potential issue is that you are sending incorrect body json or it is missing at all.\"}", responseBody)
    }

    @Test
    fun testDeleteObjectSuccess(): Unit = runBlocking {
        val url = "https://api.restful-api.dev/objects"

        val requestData = AddObjectRequest(
            name = "Apple MacBook Pro 16",
            data = AddObjectRequestData(
                year = 2019,
                price = 1849.99,
                CPUModel = "Intel Core i9",
                HardDiskSize = "1 TB"
            )
        )

        val response: AddObject = client.post(url) {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(requestData)
        }.body()

        val id = response.id

        val urlDel = "https://api.restful-api.dev/objects/$id"

        val responseDel = client.delete(urlDel) {

        }

        val responseBody: String = responseDel.body()
        assertEquals("{\"message\":\"Object with id = $id has been deleted.\"}", responseBody)
    }

    @Test
    fun testDeleteObjectFailure(): Unit = runBlocking {
        val url = "https://api.restful-api.dev/objects/6"

        val response = client.delete(url) {

        }

        val responseBody: String = response.body()
        assertTrue(responseBody.contains("\"error\""), "Expected 'error' field in the response, but got: $responseBody")
        assertEquals("{\"error\":\"6 is a reserved id and the data object of it cannot be deleted. You can create your own new object via POST request and try to send a DELETE request with new generated object id.\"}", responseBody)
    }




    @AfterTest
    fun tearDown() {
        client.close()
    }
}
