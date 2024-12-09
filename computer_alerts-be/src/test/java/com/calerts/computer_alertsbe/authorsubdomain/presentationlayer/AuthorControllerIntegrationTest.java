package com.calerts.computer_alertsbe.authorsubdomain.presentationlayer;

import com.calerts.computer_alertsbe.authorsubdomain.datalayer.Author;
import com.calerts.computer_alertsbe.authorsubdomain.datalayer.AuthorIdentifier;
import com.calerts.computer_alertsbe.authorsubdomain.datalayer.AuthorRepository;
import com.calerts.computer_alertsbe.authorsubdomain.datalayer.Biography;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port=0"})
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class AuthorControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthorRepository authorRepository;

    private Author author1 = Author.builder()
            .authorIdentifier(new AuthorIdentifier())
            .emailAddress("variable.x@email.com")
            .firstName("X")
            .lastName("Variable")
            .biography(new Biography("Lorem Ipsum."))
            .build();

    private Author author2 = Author.builder()
            .authorIdentifier(new AuthorIdentifier())
            .emailAddress("variable.y@email.com")
            .firstName("Y")
            .lastName("Variable")
            .biography(new Biography())
            .build();

    @BeforeEach
    void setUpDB() {
        Publisher<Author> setupDB = authorRepository.deleteAll()
                .thenMany(Flux.just(author1, author2)
                        .flatMap(authorRepository::save));

        StepVerifier.create(setupDB)
                .expectNext(author1)
                .expectNext(author2)
                .expectNextCount(0)
                .verifyComplete();
    }

    // Positive test case
    @Test
    void whenGetAllAuthors_thenReturnAuthors() {
        webTestClient
                .get()
                .uri("/api/v1/authors")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/event-stream;charset=UTF-8")
                .expectBodyList(AuthorResponseModel.class)
                .hasSize(2)
                .value(authorResponseModels -> {
                    assertEquals(author1.getAuthorIdentifier().getAuthorId(), authorResponseModels.get(0).getAuthorId());
                    assertEquals(author1.getEmailAddress(), authorResponseModels.get(0).getEmailAddress());
                    assertEquals(author1.getFirstName(), authorResponseModels.get(0).getFirstName());
                    assertEquals(author1.getLastName(), authorResponseModels.get(0).getLastName());
                    assertEquals(author1.getBiography().getBiography(), authorResponseModels.get(0).getBiography());

                    assertEquals(author2.getAuthorIdentifier().getAuthorId(), authorResponseModels.get(1).getAuthorId());
                    assertEquals(author2.getEmailAddress(), authorResponseModels.get(1).getEmailAddress());
                    assertEquals(author2.getFirstName(), authorResponseModels.get(1).getFirstName());
                    assertEquals(author2.getLastName(), authorResponseModels.get(1).getLastName());
                    assertEquals(author2.getBiography().getBiography(), authorResponseModels.get(1).getBiography());
                });
    }

    // Negative test case
    @Test
    void whenNoAuthorsExist_thenReturnEmptyList() {
        Publisher<Void> deleteAllAuthors = authorRepository.deleteAll();

        StepVerifier.create(deleteAllAuthors)
                .expectNextCount(0)
                .verifyComplete();

        webTestClient
                .get()
                .uri("/api/v1/authors")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/event-stream;charset=UTF-8")
                .expectBodyList(AuthorResponseModel.class)
                .value(authorResponseModels -> {
                    assertNotNull(authorResponseModels);
                    assertEquals(0, authorResponseModels.size());
                });
    }
}