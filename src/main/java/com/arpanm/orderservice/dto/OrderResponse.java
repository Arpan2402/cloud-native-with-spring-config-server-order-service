package com.arpanm.orderservice.dto;

import com.arpanm.orderservice.domain.Order;
import com.arpanm.orderservice.domain.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    Long id;
    String bookIsbn;
    String bookName;
    Double bookPrice;
    Integer quantity;
    OrderStatus status;
    Instant createdDate;
    Instant updatedDate;
    Integer version;
    String reason;

    public OrderResponse(String isbn, OrderStatus status, String reason) {
        this.bookIsbn = isbn;
        this.status = status;
        this.reason = reason;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(),
                                 order.getBookIsbn(),
                                 order.getBookName(),
                                 order.getBookPrice(),
                                 order.getQuantity(),
                                 order.getStatus(),
                                 order.getCreatedDate(),
                                 order.getUpdatedDate(),
                                 order.getVersion(),
                           null);
    }

    public static OrderResponse of(String isbn, OrderStatus status, String reason) {
        return new OrderResponse(isbn, status, reason);
    }
}
