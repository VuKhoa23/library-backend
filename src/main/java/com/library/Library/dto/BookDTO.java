package com.library.Library.dto;

import lombok.*;

@Data
@ToString
@Builder
public class BookDTO {
    String name;
    Long categoryId;
}
