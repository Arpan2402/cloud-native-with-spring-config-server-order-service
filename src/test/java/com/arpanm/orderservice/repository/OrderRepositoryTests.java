package com.arpanm.orderservice.repository;

import com.arpanm.orderservice.config.DataConfig;
import com.arpanm.orderservice.domain.Order;
import com.arpanm.orderservice.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
public class OrderRepositoryTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", OrderRepositoryTests::getR2DbcUrl);
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
        registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    }

    private static String getR2DbcUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgreSQLContainer.getDatabaseName());
    }

    @Test
    public void testCreateOrder() {
        String bookIsbn = "1234567";
        Mono<Order> orderMono = this.orderRepository.save(Order.
                of(bookIsbn, "Book-Author", 100.0, 1, OrderStatus.ACCEPTED));

        StepVerifier.create(orderMono)
                .expectNextMatches(order -> Objects.nonNull(order.getId()))
                .verifyComplete();
    }
}
