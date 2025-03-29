package ru.itmo.analyticsservice.config

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.stereotype.Component

@Component
class ContextPropertiesPrinter : ApplicationListener<ContextRefreshedEvent> {
    private companion object {
        @Suppress("ktlint:standard:property-naming")
        var DELIMITER = "-".repeat(64) + "\n"

        @Suppress("ktlint:standard:property-naming")
        var DOUBLE_DELIMITER = "=".repeat(64) + "\n"
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val env = event.applicationContext.environment as ConfigurableEnvironment

        val propertiesInfo =
            buildString {
                append("\n")
                append(DOUBLE_DELIMITER)
                append("User Service Reactive properties: \n")
                append(DOUBLE_DELIMITER)
                append("Application name ..............................${env.getProperty("spring.application.name")}\n")
                append("Version .......................................${env.getProperty("spring.application.version")}\n")
                append("Service port ..................................${env.getProperty("server.port")}\n")
                append("Active profiles ...............................${env.activeProfiles.contentToString()}\n")
                append(DELIMITER)
            }

        log.info(propertiesInfo)
    }
}
