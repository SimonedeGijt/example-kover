package org.example.greeting

import org.springframework.stereotype.Service

@Service
class GreetingService {
    fun standardGreet() = inlineFunction {
        val greeting = "Hello world"
        println(greeting)
        return@inlineFunction greeting
    }

    fun greet(s: String?) = if (s.isNullOrEmpty()) standardGreet() else inlineFunction {
        val greeting = "Hello $s"
        println(greeting)
        return@inlineFunction greeting
    }

    private final inline fun inlineFunction(myFun: () -> String) = myFun()
}
