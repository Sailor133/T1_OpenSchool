package com.sysoev.t1_openschool.dto;

import lombok.Data;

@Data
public class TaskRequestDto {
    private String title;
    private String status;
    private String description;
    private Long userId;
}
