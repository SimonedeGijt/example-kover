package org.example.greeting

import org.example.WebIntegrationTest
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

class GreetingTest : WebIntegrationTest() {
    @Test
    fun `Get greeting`() {
        mockMvc.get("/greetings") {
        }.andExpect {
            status { isOk() }
            content {
                contentType(APPLICATION_JSON)
                jsonPath<List<String>>("$", hasSize(1))
                jsonPath("$[0]", equalTo("hello"))
            }
        }.andDo { print() }
    }
}
