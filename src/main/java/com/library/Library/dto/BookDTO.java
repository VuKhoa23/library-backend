package com.library.Library.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BookDTO {
    String name;
    Long categoryId;
}
