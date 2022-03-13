package org.example.greeting

import org.springframework.stereotype.Service

@Service
class GreetingService {
    var storedGreetings = listOf("hello")

    fun getGreetings(): List<String> = storedGreetings
}
