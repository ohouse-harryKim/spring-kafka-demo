package se.ohou.springkafkademo

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Component
class TestPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {

    @Value("\${kafka.topic}")
    private lateinit var topic: String

    fun publish(data: String?) = kafkaTemplate.send(topic, data)
}

@RequestMapping
@RestController
class TestController(
    private val testPublisher: TestPublisher
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun index(@RequestBody body: Payload) {
        testPublisher.publish(body.message)
    }

    data class Payload(
        val message: String?
    )
}
