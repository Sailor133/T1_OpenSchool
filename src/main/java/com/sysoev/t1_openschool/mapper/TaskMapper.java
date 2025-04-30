package com.sysoev.t1_openschool.mapper;

import com.sysoev.t1_openschool.dto.task.request.TaskRequestDto;
import com.sysoev.t1_openschool.dto.task.response.TaskResponseDto;
import com.sysoev.t1_openschool.model.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper {
    public Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setUserId(dto.getUserId());
        return task;
    }

    public TaskResponseDto toDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setUserId(task.getUserId());
        return dto;
    }
}
