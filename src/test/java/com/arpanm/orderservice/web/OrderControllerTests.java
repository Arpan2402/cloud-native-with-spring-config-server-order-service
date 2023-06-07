package com.arpanm.orderservice.web;

import com.arpanm.orderservice.domain.OrderStatus;
import com.arpanm.orderservice.dto.OrderRequest;
import com.arpanm.orderservice.dto.OrderResponse;
import com.arpanm.orderservice.service.OrderService;
import com.arpanm.orderservice.service.OrderServiceTests;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class)
public class OrderControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        OrderRequest orderRequest = new OrderRequest("1234567", 3);
        BDDMockito.when(this.orderService.saveNewOrder(orderRequest))
                .thenReturn(Mono.just(OrderResponse.of(orderRequest.getBookIsbn(), OrderStatus.ACCEPTED, null)));

        this.webTestClient.post()
                .uri("/orders/")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(OrderResponse.class).value(order -> {
                    Assertions.assertThat(order).isNotNull();
                    Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
                });
    }
}
