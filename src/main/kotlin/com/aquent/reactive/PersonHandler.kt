package com.aquent.reactive

import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono

@Suppress("UNUSED_PARAMETER")
class PersonHandler(
        private val repository: PersonRepository,
        private val properties: DemoProperties
) {

    fun view(request: ServerRequest) =
            ok().render("view", mapOf(
                    "message" to properties.message,
                    "people" to repository.getAll()
            ))

    fun getPerson(request: ServerRequest) =
            repository.get(request.pathVariable("id").toInt())
                    .flatMap(ok().contentType(APPLICATION_JSON_UTF8)::syncBody)
                    .switchIfEmpty(notFound().build())

    fun addPerson(request: ServerRequest) =
            request.bodyToMono<Person>()
                    .flatMap(repository::insert)
                    .flatMap {
                        val uri = request.uriBuilder().pathSegment("{id}").build(it.personId)
                        created(uri).syncBody(it)
                    }

    fun savePerson(request: ServerRequest) =
            request.bodyToMono<Person>()
                    .flatMap {
                        it.personId = request.pathVariable("id").toInt()
                        repository.update(it)
                    }
                    .flatMap(ok().contentType(APPLICATION_JSON_UTF8)::syncBody)
                    .switchIfEmpty(notFound().build())

    fun deletePerson(request: ServerRequest) =
            repository.delete(request.pathVariable("id").toInt())
                    .flatMap { if (it == 0) notFound().build() else ok().build() }

    fun count(request: ServerRequest) =
            ok().contentType(APPLICATION_JSON_UTF8).body(repository.count())
}