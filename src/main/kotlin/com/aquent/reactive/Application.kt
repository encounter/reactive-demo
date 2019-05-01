package com.aquent.reactive

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.r2dbc.r2dbcPostgresql
import org.springframework.fu.kofu.reactiveWebApplication
import org.springframework.fu.kofu.webflux.thymeleaf
import org.springframework.fu.kofu.webflux.webFlux

data class DemoProperties(var message: String)

val app = reactiveWebApplication {
    configurationProperties<DemoProperties>("demo")
    beans {
        bean<PersonRepository>()
        bean<PersonHandler>()
    }
    listener<ApplicationReadyEvent> {
        ref<PersonRepository>().init().block()
    }
    webFlux {
        router {
            val personHandler = ref<PersonHandler>()
            GET("/", personHandler::view)
            POST("/person", personHandler::addPerson)
            GET("/person/count", personHandler::count)
            GET("/person/{id}", personHandler::getPerson)
            PUT("/person/{id}", personHandler::savePerson)
            DELETE("/person/{id}", personHandler::deletePerson)
        }
        codecs {
            string()
            jackson()
        }
        thymeleaf {
            cache = false
        }
    }
    r2dbcPostgresql {
        host = "localhost"
        database = "postgres"
        username = "spring"
        password = "spring-test"
    }
}

fun main() {
    app.run()
}