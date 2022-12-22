package se.ohou.springkafkademo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class ErrorHandlerConfig : Loggable {

    @Bean("defaultErrorHandler")
    fun defaultErrorHandler(kafkaTemplate: KafkaTemplate<*, *>) = DefaultErrorHandler(
        DeadLetterPublishingRecoverer(kafkaTemplate),
        FixedBackOff(1000, 3)
    )
}
