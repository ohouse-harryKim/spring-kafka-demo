package se.ohou.springkafkademo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.KafkaListenerErrorHandler

@Configuration
class ErrorHandlerConfig : Loggable {

    @Bean("customErrorHandler")
    fun customErrorHandler(recoverer: DeadLetterPublishingRecoverer) = KafkaListenerErrorHandler { message, exception ->
        log.error("#customErrorHandler received error: {}", message)
        // do recoverer.accept() or something
//        recoverer.accept( , exception)
    }

    @Bean
    fun recoverer(kafkaTemplate: KafkaTemplate<*, *>) = DeadLetterPublishingRecoverer(kafkaTemplate)
}
