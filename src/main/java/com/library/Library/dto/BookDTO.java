package com.library.Library.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class BookDTO {
    String name;
    Long categoryId;
}
