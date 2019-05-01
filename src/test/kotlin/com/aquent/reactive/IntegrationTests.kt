package com.aquent.reactive

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.test.web.reactive.server.WebTestClient

class IntegrationTests {

	private val client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()

	private lateinit var context: ConfigurableApplicationContext

	@BeforeAll
	fun before() {
		context = app.run()
	}

	@Test
	fun `Request HTML endpoint`() {
		client.get().uri("/").exchange()
				.expectStatus().is2xxSuccessful
				.expectHeader().contentType(TEXT_HTML)

	}

	@Test
	fun `Request HTTP API endpoint`() {
		client.get().uri("/person/count").exchange()
				.expectStatus().is2xxSuccessful
				.expectHeader().contentType(APPLICATION_JSON_UTF8_VALUE)
	}

	@AfterAll
	fun after() {
		context.close()
	}
}