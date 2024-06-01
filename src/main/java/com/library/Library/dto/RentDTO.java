package com.library.Library.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RentDTO {
    Long userId;
    Long bookId;
    Date endDate;
}
