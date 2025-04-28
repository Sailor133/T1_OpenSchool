package com.sysoev.t1_openschool.service;

import com.sysoev.t1_openschool.aspect.annotation.ExecutionTimeMeasuring;
import com.sysoev.t1_openschool.model.Task;
import com.sysoev.t1_openschool.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @ExecutionTimeMeasuring
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    @ExecutionTimeMeasuring
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }
    @ExecutionTimeMeasuring
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    @ExecutionTimeMeasuring
    public Task updateTask(Long id, Task task) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setId(existing.getId());
        return taskRepository.save(task);
    }
    @ExecutionTimeMeasuring
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
