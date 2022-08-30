package se.ohou.springkafkademo

import org.springframework.beans.factory.InitializingBean
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import se.ohou.springkafkademo.models.TopicValue

@Component
class TestListener : Loggable, InitializingBean {

    @KafkaListener(
        groupId = "\${spring.application.name}",
        topics = ["\${kafka.topic}"],
    )
    fun listen(@Payload payload: TopicValue) {
        log.debug("#listen: {}", payload)
    }

    override fun afterPropertiesSet() {
        log.debug("#listening start.")
    }
}
