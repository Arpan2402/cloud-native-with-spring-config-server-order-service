package com.arpanm.orderservice.client;

import com.arpanm.orderservice.book.BookDto;
import com.arpanm.orderservice.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class BookClient {

    private static final String BOOKS = "/v1/books/";

    private final WebClient webClient;

    public Mono<BookDto> getBookByIsbn(String isbn) {
        return this.webClient
                .get()
                .uri(BOOKS + isbn)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(ClientException::new))
                .bodyToMono(BookDto.class)
                .timeout(Duration.ofSeconds(2), Mono.empty())
                .retryWhen(Retry.backoff(3, Duration.ofMillis(1000))
                        .filter(throwable -> !(throwable instanceof ClientException)))
                .onErrorResume(Exception.class, ex -> Mono.error(ex));
    }
}
