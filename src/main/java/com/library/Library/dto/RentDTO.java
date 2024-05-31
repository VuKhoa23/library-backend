package com.library.Library.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RentDTO {
    String username;
    String bookName;
    Date endDate;
}
