package com.arpanm.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Book ISBN cannot be empty")
    private String bookIsbn;

    @NotNull(message = "Book Quantity cannot be empty")
    @Min(value = 1, message = "Quantity cannot be less than 1")
    @Max(value = 5, message = "Quantity cannot be more than 5")
    private Integer bookQuantity;
}
