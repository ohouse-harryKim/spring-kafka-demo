package se.ohou.springkafkademo

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff
import java.lang.Exception

@Configuration
class ErrorHandlerConfig : Loggable {

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        defaultErrorHandler: DefaultErrorHandler
    ) =
        ConcurrentKafkaListenerContainerFactory<String, String>()
            .apply {
                this.consumerFactory = consumerFactory
                this.setCommonErrorHandler(defaultErrorHandler)
            }

    @Bean("defaultErrorHandler")
    fun defaultErrorHandler(recoverer: DeadLetterPublishingRecoverer) = DefaultErrorHandler(
        recoverer,
        FixedBackOff(1000, 3)
    )

    @Bean
    fun recoverer(kafkaTemplate: KafkaTemplate<*, *>) = object : DeadLetterPublishingRecoverer(kafkaTemplate) {
        override fun accept(record: ConsumerRecord<*, *>, exception: Exception) {
            log.error(
                """
                ${record.topic()} consume Failure.
                cause: ${exception.message}
                message key: ${record.key()}
                message value: ${record.value()}
                """.trimIndent()
            )
            super.accept(record, exception)
        }
    }
}
