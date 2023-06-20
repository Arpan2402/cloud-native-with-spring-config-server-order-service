package com.arpanm.orderservice.service;

import com.arpanm.orderservice.CustomStreamBridge;
import com.arpanm.orderservice.book.BookDto;
import com.arpanm.orderservice.client.BookClient;
import com.arpanm.orderservice.domain.Order;
import com.arpanm.orderservice.domain.OrderStatus;
import com.arpanm.orderservice.dto.OrderRequest;
import com.arpanm.orderservice.dto.OrderResponse;
import com.arpanm.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@Import(TestChannelBinderConfiguration.class)
@ContextConfiguration(classes = OrderService.class)
public class OrderServiceTests {
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private BookClient bookClient;
    @MockBean
    private CustomStreamBridge streamBridge;
    @Autowired
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        String bookIsbn = "1234567";
        OrderRequest orderRequest = new OrderRequest(bookIsbn, 2);
        BookDto bookDto = new BookDto(bookIsbn, "Title", "Author", 10.0, null);
        Order order = Order.of(
                bookDto.getIsbn(),
                bookDto.getTitle() + "-" + bookDto.getAuthor(),
                bookDto.getPrice(),
                orderRequest.getBookQuantity(),
                OrderStatus.ACCEPTED
                );

        BDDMockito.when(bookClient.getBookByIsbn(bookIsbn)).thenReturn(Mono.just(bookDto));
        BDDMockito.when(orderRepository.save(Mockito.any())).thenReturn(Mono.just(order));

        Mono<OrderResponse> orderResponseMono = this.orderService.saveNewOrder(orderRequest);

        StepVerifier.create(orderResponseMono)
                .expectNextMatches(orderResponse -> orderResponse.getStatus().equals(OrderStatus.ACCEPTED))
                .verifyComplete();
    }
}
