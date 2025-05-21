package com.sysoev.t1_openschool.dto;

import com.sysoev.t1_openschool.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponseDto {
    private Long id;
    private String title;
    private TaskStatus status;
    private String description;
    private Long userId;
}
