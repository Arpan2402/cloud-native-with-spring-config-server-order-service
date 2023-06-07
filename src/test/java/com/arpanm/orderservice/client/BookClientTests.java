package com.arpanm.orderservice.client;

import com.arpanm.orderservice.book.BookDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Optional;

public class BookClientTests {

    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    public void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        WebClient webClient = WebClient
                .builder()
                .baseUrl(this.mockWebServer.url("/").uri().toString())
                .build();

        this.bookClient = new BookClient(webClient);
    }

    @AfterEach
    public void destroy() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    public void testGetBookByIsbnShouldPass() throws JsonProcessingException {
        String bookIsbn = "1234567";
        BookDto bookDto = new BookDto(bookIsbn, "Title", "Author", 9.90, null);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(bookDto);

        MockResponse response = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json);
        this.mockWebServer.enqueue(response);

        Mono<BookDto> bookDtoMono = this.bookClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(bookDtoMono)
                .expectNextMatches(book -> book.getIsbn().equals(bookIsbn))
                .verifyComplete();
    }
}
