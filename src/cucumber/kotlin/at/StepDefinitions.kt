package at

import io.cucumber.datatable.DataTable
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.test.web.reactive.server.expectBody
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Duration
import java.util.function.Consumer
import java.util.stream.Collectors


//@Testcontainers
@ContextConfiguration(classes = [CucumberConfiguration::class])
class StepDefinitions {
//    companion object {
//        var dockerComposeContainer: DockerComposeContainer<*> =
//            DockerComposeContainer<Nothing>(File("docker-compose.yml"))
//                .waitingFor("myservice", HostPortWaitStrategy())
//        @JvmStatic
//        @Before
//        fun beforeAll() {
//            dockerComposeContainer.start()
//        }
//    }

    private lateinit var lastResponse: Response

    @Given("the following pets are available in the store:")
    fun the_following_pets_are_available_in_the_store(dataTable: DataTable) {
//        PetCreateRequest("", "","", "", "")
    lastResponse = RestAssured
        .get("http://127.0.0.1:9000/catalog/search?petName=Nemo2")
    assertThat(lastResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value())

//            .get(env.getProperty("daily.path"))

//        convertDataTableToJsonArray(dataTable).forEach {
//            val next: JSONObject = it as JSONObject
//            webTestClient.post()
//                .uri("/catalog/search")
//                .contentType(APPLICATION_JSON)
//                .body(
//                    fromValue(
//                        next.toString()
//                    )
//                )
//                .exchange()
//        }
    }

    //TODO propertysource .... am I handling that in Java? how do I handle it in kotlinb?
    @When("I select {string}")
    fun i_select(name: String?) {
//        response = webTestClient.get()
//            .uri { builder ->
//                builder.path("/catalog/search")
//                    //TODO will plug into a datajap @Query? or get all and filter?
//                    .queryParam("petName", name)
//                    .build()
//            }
//            .exchange()
//            .expectStatus()
//            .isOk
    }

    @Then("the following data should be returned:")
    fun the_following_data_should_be_returned(dataTable: DataTable) {
//        var expected = JSONObject(convertDataTableToJsonArray(dataTable)[0].toString())
//        val actual = convertResponseToJSONObject(response)
//        JSONAssert.assertEquals(expected, actual, LENIENT)
        // what about checking on response that ids that went into testdataholder previously?
        // just use 'response' object to hold stuff!!!
    }

    private fun convertResponseToJSONObject(response: ResponseSpec): JSONObject {
        // no need to ignore certain properties see -> https://www.baeldung.com/jsonassert
        val responseBodySpec = response.expectBody<String>()
        val responseEntityExchangeResult = responseBodySpec.returnResult()
        val responseString = responseEntityExchangeResult.responseBody
        return JSONObject(responseString)
    }

    @Throws(JSONException::class)
    private fun convertDataTableToJsonArray(
        dataTable: DataTable,
        vararg columnsToIgnore: String
    ): JSONArray {
        return JSONArray(dataTable
            .asMaps()
            .stream()
            .map { map: Map<String, String> ->
                removeColumns(
                    map,
                    *columnsToIgnore
                )
            }
            .collect(Collectors.toList()))
    }

    private fun removeColumns(
        originalMapOfDataTable: Map<String, String>,
        vararg columnsToIgnore: String
    ): HashMap<String, String> {
        val modifiedMap = HashMap(originalMapOfDataTable)
        listOf(*columnsToIgnore)
            .forEach(Consumer { key: String? -> modifiedMap.remove(key) })
        return modifiedMap
    }
}

