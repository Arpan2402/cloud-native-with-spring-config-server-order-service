package com.arpanm.orderservice.service;

import com.arpanm.orderservice.CustomStreamBridge;
import com.arpanm.orderservice.book.BookDto;
import com.arpanm.orderservice.client.BookClient;
import com.arpanm.orderservice.domain.Order;
import com.arpanm.orderservice.domain.OrderStatus;
import com.arpanm.orderservice.dto.OrderAcceptedMessage;
import com.arpanm.orderservice.dto.OrderDispatchedMessage;
import com.arpanm.orderservice.dto.OrderRequest;
import com.arpanm.orderservice.dto.OrderResponse;
import com.arpanm.orderservice.exception.InvalidOrderException;
import com.arpanm.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookClient bookClient;
    private final CustomStreamBridge streamBridge;

    public Flux<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Transactional
    public Mono<OrderResponse> saveNewOrder(OrderRequest orderRequest) {
        return this.bookClient.getBookByIsbn(orderRequest.getBookIsbn())
                .map(book -> createAcceptedOrder(book, orderRequest))
                .flatMap(this.orderRepository::save)
                .doOnNext(this::publishAcceptedOrderAcceptedQueue)
                .map(OrderResponse::of)
                .onErrorResume(Exception.class, ex -> Mono.error(() -> new InvalidOrderException(ex.getMessage())));
    }

    public Flux<Order> updateDispatchStatus(Flux<OrderDispatchedMessage> orderDispatchedMessage) {
        return orderDispatchedMessage
                .doOnNext(orderDispatched -> log.info("Order for Dispatching received with id - {}", orderDispatched.getOrderId()))
                .flatMap(orderDispatched -> orderRepository.findById(orderDispatched.getOrderId()))
                .map(this::createDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    private Order createAcceptedOrder(BookDto bookDto, OrderRequest orderRequest) {
        return Order.of(bookDto.getIsbn(),
                bookDto.getTitle() + "-" + bookDto.getAuthor(),
                bookDto.getPrice(),
                orderRequest.getBookQuantity(),
                OrderStatus.ACCEPTED);
    }

    private Order createDispatchedOrder(Order existingOrder) {
        return new Order(
                existingOrder.getId(),
                existingOrder.getBookIsbn(),
                existingOrder.getBookName(),
                existingOrder.getBookPrice(),
                existingOrder.getQuantity(),
                OrderStatus.DISPATCHED,
                existingOrder.getCreatedDate(),
                existingOrder.getUpdatedDate(),
                existingOrder.getVersion());
    }

    private void publishAcceptedOrderAcceptedQueue(Order order) {
        log.info("Sending Accepted Order to order-accepted exchange");
        OrderAcceptedMessage orderAcceptedMessage = new OrderAcceptedMessage(order.getId());
        this.streamBridge.sendMessage("acceptOrder-out-0", orderAcceptedMessage);
        log.info("Sent Accepted Order to order-accepted exchange");
    }
}