package com.arpanm.orderservice.service;

import com.arpanm.orderservice.book.BookDto;
import com.arpanm.orderservice.client.BookClient;
import com.arpanm.orderservice.domain.Order;
import com.arpanm.orderservice.domain.OrderStatus;
import com.arpanm.orderservice.dto.OrderRequest;
import com.arpanm.orderservice.dto.OrderResponse;
import com.arpanm.orderservice.exception.InvalidOrderException;
import com.arpanm.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final BookClient bookClient;

    public Flux<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Mono<OrderResponse> saveNewOrder(OrderRequest orderRequest) {
        return this.bookClient.getBookByIsbn(orderRequest.getBookIsbn())
                .map(book -> createAcceptedOrder(book, orderRequest))
                .flatMap(this.orderRepository::save)
                .map(OrderResponse::of)
                .onErrorResume(Exception.class, ex -> Mono.error(() -> new InvalidOrderException(ex.getMessage())));
    }

    private Order createAcceptedOrder(BookDto bookDto, OrderRequest orderRequest) {
        return Order.of(bookDto.getIsbn(),
                bookDto.getTitle() + "-" + bookDto.getAuthor(),
                bookDto.getPrice(),
                orderRequest.getBookQuantity(),
                OrderStatus.ACCEPTED);
    }
}