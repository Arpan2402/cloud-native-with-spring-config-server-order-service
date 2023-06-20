package com.arpanm.orderservice.config;

import com.arpanm.orderservice.dto.OrderDispatchedMessage;
import com.arpanm.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class FunctionsConfig {

    @Bean
    public Consumer<Flux<OrderDispatchedMessage>> dispatchOrder(OrderService orderService) {
        return orderDispatchedMessageFlux ->
                orderService.updateDispatchStatus(orderDispatchedMessageFlux)
                        .doOnNext(order -> log.info("Order id - {} updated to dispatched status", order.getId()))
                        .subscribe();
    }
}
