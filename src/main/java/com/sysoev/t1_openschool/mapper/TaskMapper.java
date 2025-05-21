package com.sysoev.t1_openschool.mapper;

import com.sysoev.t1_openschool.dto.KafkaUpdatingDto;
import com.sysoev.t1_openschool.dto.TaskRequestDto;
import com.sysoev.t1_openschool.dto.TaskResponseDto;
import com.sysoev.t1_openschool.model.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper {
    public Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setStatus(dto.getStatus());
        task.setDescription(dto.getDescription());
        task.setUserId(dto.getUserId());
        return task;
    }

    public TaskResponseDto toDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setStatus(task.getStatus());
        dto.setDescription(task.getDescription());
        dto.setUserId(task.getUserId());
        return dto;
    }

    public KafkaUpdatingDto toKafkaDto(Task task) {
        KafkaUpdatingDto dto = new KafkaUpdatingDto();
        dto.setId(task.getId());
        dto.setStatus(task.getStatus());
        return dto;
    }
}
