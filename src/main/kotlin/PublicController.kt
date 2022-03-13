package org.example

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PublicController {
    @GetMapping("/greetings")
    fun homepage(): List<String> = listOf("hello")
}
