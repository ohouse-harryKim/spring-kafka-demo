package se.ohou.springkafkademo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringKafkaDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringKafkaDemoApplication>(*args)
}

interface Loggable {
    val log: Logger get() = LoggerFactory.getLogger(javaClass)
}
