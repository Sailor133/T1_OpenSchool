package com.sysoev.t1_openschool.service;

import com.sysoev.t1_openschool.aspect.annotation.ExecutionTimeMeasuring;
import com.sysoev.t1_openschool.kafka.KafkaTaskProducer;
import com.sysoev.t1_openschool.dto.TaskRequestDto;
import com.sysoev.t1_openschool.dto.TaskResponseDto;
import com.sysoev.t1_openschool.exeption.TaskNotFoundException;
import com.sysoev.t1_openschool.mapper.TaskMapper;
import com.sysoev.t1_openschool.model.Task;
import com.sysoev.t1_openschool.model.enums.TaskStatus;
import com.sysoev.t1_openschool.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaTaskProducer kafkaProducer;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, KafkaTaskProducer kafkaProducer) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.kafkaProducer = kafkaProducer;
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
    public TaskResponseDto updateTask(Long id, TaskRequestDto enterTask) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        TaskStatus oldStatus = existing.getStatus();

        existing.setTitle(enterTask.getTitle());
        existing.setDescription(enterTask.getDescription());
        existing.setStatus(enterTask.getStatus());

        Task saved = taskRepository.save(existing);

        if (!Objects.equals(oldStatus, enterTask.getStatus())) {
            kafkaProducer.sendChangedTaskStatus(taskMapper.toKafkaDto(saved));
        }

        return taskMapper.toDto(saved);
    }


    @ExecutionTimeMeasuring
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
