package com.sysoev.t1_openschool.controller;

import com.sysoev.t1_openschool.aspect.annotation.LogEnterTask;
import com.sysoev.t1_openschool.dto.TaskRequestDto;
import com.sysoev.t1_openschool.dto.TaskResponseDto;
import com.sysoev.t1_openschool.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @LogEnterTask
    public TaskResponseDto createTask(@RequestBody TaskRequestDto requestTaskDto) {
        return taskService.createTask(requestTaskDto);
    }

    @PutMapping("/{id}")
    @LogEnterTask
    public TaskResponseDto updateTask(@PathVariable Long id, @RequestBody TaskRequestDto requestTaskDto) {
        return taskService.updateTask(id, requestTaskDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
