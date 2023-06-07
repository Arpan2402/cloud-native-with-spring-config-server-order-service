package com.arpanm.orderservice.web;

import com.arpanm.orderservice.domain.Order;
import com.arpanm.orderservice.dto.OrderRequest;
import com.arpanm.orderservice.dto.OrderResponse;
import com.arpanm.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Flux<Order> getAllOrders() {
        return this.orderService.getAllOrders();
    }

    @PostMapping
    public Mono<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return this.orderService.saveNewOrder(orderRequest);
    }
}
