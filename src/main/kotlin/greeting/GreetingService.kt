package org.example.greeting

import org.springframework.stereotype.Service

private const val HELLO = "Hello"

@Service
class GreetingService {
    fun standardGreet() = "$HELLO peeps!"

    fun greet(s: String?) = if (s.isNullOrEmpty()) standardGreet() else "$HELLO $s!"

    final inline fun getWorld() = "world"
}
