package com.arpanm.orderservice.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String isbn;
    private String title;
    private String author;
    private Double price;
    private String reason;

    public BookDto(String isbn, String reason) {
        this.isbn = isbn;
        this.reason = reason;
    }
}
