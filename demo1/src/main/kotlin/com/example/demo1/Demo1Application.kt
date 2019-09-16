package com.example.demo1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

// this example is only for test Istio
// please try do not analyse this code, probably will be with code smells a lot

@SpringBootApplication
class Demo1Application

fun main(args: Array<String>) {
    runApplication<Demo1Application>(*args)
}


@RestController
class WelcomeController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @RequestMapping("/welcome")
    fun welcome() = "Welcome service 2"

    @RequestMapping("/welcome-hello")
    fun welcomeHello(): String? {

        var message: String = ""

        try {
            message = this.restTemplate.getForObject("http://hello-message-app:8081/hello", String.javaClass).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            println("Exception " + e.message)
            message = "exception here"
        }

        return message
    }

    @Bean
    fun rest(): RestTemplate {
        return RestTemplate()
    }

}