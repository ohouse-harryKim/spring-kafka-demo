package se.ohou.springkafkademo

import org.springframework.beans.factory.InitializingBean
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class TestListener : Loggable, InitializingBean {

    @KafkaListener(
        groupId = "\${spring.application.name}",
        topics = ["\${kafka.topic}"],
        errorHandler = "customErrorHandler"
    )
    fun listen(@Payload payload: String) {
        log.debug("#listen: {}", payload)
        if (payload.contains("error")) {
            throw Exception("에러 테스트")
        }
    }

    override fun afterPropertiesSet() {
        log.debug("#listening start.")
    }
}
