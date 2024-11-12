package com.mutant.dna;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.io.IOException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DnaApplication.class})
class DnaApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	private String schemaSql;
	private String dataSql;

	@Autowired
	private DatabaseClient databaseClient;

	@BeforeEach
	public void setup() throws IOException {


	}

	@Test
	public void tesMutantDna() {
		String requestBody = "{\"dna\": [\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}";

		webTestClient.post()
				.uri("/mutant")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.exchange()
				.expectStatus().isOk();

		webTestClient.post()
				.uri("/mutant")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testNoMutantDna() {
		String requestBody = "{\"dna\": [\"ATGCGA\",\"CAGTGC\",\"TTGTGT\",\"AGAAGG\",\"CCGCTA\",\"TCACTG\"]}";

		webTestClient.post()
				.uri("/mutant")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.exchange()
				.expectStatus().is4xxClientError();

	}



	@Test
	public void testExeptioNoMutantDna() {
		String requestBody = "{\"dna\": [\"ATPCGA\",\"CAGTGC\",\"TTGTGT\",\"AGAAGG\",\"CCGCTA\",\"TCACTG\"]}";

		webTestClient.post()
				.uri("/mutant")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.exchange()
				.expectStatus().is4xxClientError()
				.expectBody()
				.jsonPath("$.message").isEqualTo("La secuencia contiene caracteres no permitidos. Solo se permiten las letras A, T, C, G. Valor encontrado P");;

	}

	@Test
	public void testStatsMutantDna() {

		webTestClient.get()
				.uri("/stats")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.ratio").isEqualTo("0.6666666666666666");
	}

}
