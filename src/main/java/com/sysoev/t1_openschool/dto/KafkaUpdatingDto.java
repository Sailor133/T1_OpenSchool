package com.sysoev.t1_openschool.dto;

import com.sysoev.t1_openschool.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaUpdatingDto {
    private Long id;
    private TaskStatus status;

}