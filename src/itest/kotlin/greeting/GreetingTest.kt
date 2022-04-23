package org.example.greeting

import org.example.WebIntegrationTest
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.get

class GreetingTest : WebIntegrationTest() {
    @Test
    fun `Get greeting`() {
        mockMvc.get("/greeting") {
        }.andExpect {
            status { isOk() }
            content {
                contentType(APPLICATION_JSON)
                jsonPath("$", equalTo("Hello world"))
            }
        }.andDo { print() }
    }
}
