package at

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.json.JSONArray
import org.json.JSONObject
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode.LENIENT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters.fromValue
import java.util.function.Consumer
import java.util.stream.Collectors

class StepDefinitions(
    @Autowired private val webClient: WebTestClient
) {
    private lateinit var response: ResponseSpec

    @Given("the following pets are available in the store:")
    fun the_following_pets_are_available_in_the_store(dataTable: DataTable) {
        convertDataTableToJsonArray(dataTable).forEach {
            val next: JSONObject = it as JSONObject
            webClient.post()
                .uri("/catalog/pets")
                .contentType(APPLICATION_JSON)
                .body(
                    fromValue(
                        next.toString()
                    )
                )
                .exchange()
        }
    }

    @When("I select {string}")
    fun i_select(name: String?) {
        response = webClient.get()
            .uri { builder ->
                builder.path("/catalog/pets")
                    .queryParam("petName", name)
                    .build()
            }
            .exchange()
            .expectStatus()
            .isOk
    }

    @Then("the following data should be returned:")
    fun the_following_data_should_be_returned(dataTable: DataTable) {
        var expected = JSONObject(convertDataTableToJsonArray(dataTable)[0].toString())
        val actual = convertResponseToJSONObject(response)
        JSONAssert.assertEquals(expected, actual, LENIENT)
    }

    private fun convertResponseToJSONObject(response: ResponseSpec): JSONObject {
        val responseBodySpec = response.expectBody<String>()
        val responseEntityExchangeResult = responseBodySpec.returnResult()
        val responseString = responseEntityExchangeResult.responseBody
        return JSONObject(responseString)
    }

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
