package org.example.greeting

import org.springframework.stereotype.Service

@Service
class GreetingService {
    fun standardGreet() = "Hello ${getWorld()}!"

    fun greet(s: String) = "Hello $s!"

    final inline fun getWorld() = "world"
}
