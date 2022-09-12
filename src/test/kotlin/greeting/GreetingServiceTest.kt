package org.example.greeting

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GreetingServiceTest {

    @Test
    fun `The GreetingService will return an empty list before any messages have been stored`() {
        val service = GreetingService()
        assertThat(service.greet("UtrechtJUG")).isEqualTo("Hello UtrechtJUG")
    }
}
