package com.arpanm.orderservice.repository;

import com.arpanm.orderservice.domain.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}
