package org.example

import org.example.greeting.GreetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PublicController(@Autowired(required = true) val service: GreetingService) {

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/greeting"],
        produces = ["application/json"]
    )
    fun homepage(): ResponseEntity<String> = ResponseEntity(service.standardGreet(), HttpStatus.valueOf(200))
}
