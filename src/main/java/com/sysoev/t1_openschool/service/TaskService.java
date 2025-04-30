package com.sysoev.t1_openschool.service;

import com.sysoev.t1_openschool.aspect.annotation.ExecutionTimeMeasuring;
import com.sysoev.t1_openschool.dto.task.request.TaskRequestDto;
import com.sysoev.t1_openschool.dto.task.response.TaskResponseDto;
import com.sysoev.t1_openschool.exeption.TaskNotFoundException;
import com.sysoev.t1_openschool.mapper.TaskMapper;
import com.sysoev.t1_openschool.model.Task;
import com.sysoev.t1_openschool.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @ExecutionTimeMeasuring
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @ExecutionTimeMeasuring
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return taskMapper.toDto(task);
    }

    @ExecutionTimeMeasuring
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task task = taskMapper.toEntity(taskRequestDto);
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @ExecutionTimeMeasuring
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        Task updatedTask = taskMapper.toEntity(taskRequestDto);
        updatedTask.setId(existing.getId());

        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    @ExecutionTimeMeasuring
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
