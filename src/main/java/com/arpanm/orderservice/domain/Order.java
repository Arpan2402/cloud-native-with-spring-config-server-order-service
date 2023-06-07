package com.arpanm.orderservice.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table("orders")
public class Order {
    @Id
    Long id;
    String bookIsbn;
    String bookName;
    Double bookPrice;
    Integer quantity;
    OrderStatus status;
    @CreatedDate
    Instant createdDate;
    @LastModifiedDate
    Instant updatedDate;
    @Version
    int version;

    public static Order of(String isbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status) {
        return new Order(null, isbn, bookName, bookPrice, quantity, status, null, null, 0);
    }
}
