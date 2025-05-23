package com.sysoev.t1_openschool.service;

import com.sysoev.t1_openschool.dto.TaskRequestDto;
import com.sysoev.t1_openschool.dto.TaskResponseDto;
import com.sysoev.t1_openschool.exeption.TaskNotFoundException;
import com.sysoev.t1_openschool.kafka.KafkaTaskProducer;
import com.sysoev.t1_openschool.mapper.TaskMapper;
import com.sysoev.t1_openschool.model.Task;
import com.sysoev.t1_openschool.model.enums.TaskStatus;
import com.sysoev.t1_openschool.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepositoryMock;
    @Mock
    private KafkaTaskProducer kafkaTaskProducerMock;
    private TaskMapper taskMapper;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
        taskService = new TaskService(taskRepositoryMock, taskMapper, kafkaTaskProducerMock);
    }

    @Test
    @DisplayName("Успешный тест отображения всех существующих тасков")
    void getAllTasks() {

        List<Task> listTestTasks = createListTestTasks();

        when(taskRepositoryMock.findAll()).thenReturn(listTestTasks);

        List<TaskResponseDto> tasks = taskService.getAllTasks();

        assertEquals(10, tasks.size());
        assertEquals("Супер задача: 1", tasks.get(0).getTitle());
    }

    @DisplayName("Успешный тест отображения таска по ID")
    @Test
    void getTaskById() {

        Task testTask = createTestTask();

        when(taskRepositoryMock.findById(any())).thenReturn(Optional.of(testTask));

        TaskResponseDto taskById = taskService.getTaskById(1L);

        assertNotNull(taskById);
        assertEquals("Супер задача", taskById.getTitle());
        assertEquals("Описание супер важной задачи", taskById.getDescription());
        assertEquals(TaskStatus.NEW, taskById.getStatus());
        assertEquals(8L, taskById.getUserId());
    }

    @DisplayName("НЕуспешный тест отображения таска по ID, когда нет такого таска")
    @Test
    void getTaskById_BadRequest() {
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(18888L));
    }

    @DisplayName("Успешный тест создания таска")
    @Test
    void createTask() {
        TaskRequestDto testRequestDto = createTestRequestDto();

        // Для сравнения симулируем сохранение в БД, присвоив ID
        Task expectedTask = taskMapper.toEntity(testRequestDto);
        expectedTask.setId(1L);

        when(taskRepositoryMock.save(any())).thenReturn(expectedTask);

        TaskResponseDto savedTask = taskService.createTask(testRequestDto);

        assertNotNull(savedTask);
        assertEquals("Супер задача для RequestDto", savedTask.getTitle());
        assertEquals("Описание задачи для RequestDto", savedTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, savedTask.getStatus());
        assertEquals(12L, savedTask.getUserId());
    }

    @DisplayName("Успешный тест обновления статуса")
    @Test
    void updateTask() {
        Task existing = createTestTask();
        TaskRequestDto updateDto = createTestRequestDto();
        updateDto.setStatus(TaskStatus.DONE);

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepositoryMock.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponseDto result = taskService.updateTask(1L, updateDto);

        assertNotNull(result);
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @DisplayName("НЕуспешный тест обновления статуса, когда нет такого таска")
    @Test
    void updateTask_shouldThrowException_whenNotFound() {
        TaskRequestDto dto = createTestRequestDto();

        Long testTaskId = 42L;
        when(taskRepositoryMock.findById(any()))
                .thenThrow(new TaskNotFoundException("Task not found with id: " + testTaskId));


       assertThrows(TaskNotFoundException.class, () ->taskService.updateTask(testTaskId, dto));
    }

    @DisplayName("Успешный тест запуска удаления таски")
    @Test
    void deleteTask() {
        doNothing().when(taskRepositoryMock).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepositoryMock).deleteById(1L);
    }

    private Task createTestTask() {
        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Супер задача");
        testTask.setStatus(TaskStatus.NEW);
        testTask.setDescription("Описание супер важной задачи");
        testTask.setUserId(8L);

        return testTask;
    }

    private List<Task> createListTestTasks() {
        List<Task> taskList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Task testTask = new Task();
            testTask.setId((long) i);
            testTask.setTitle("Супер задача: " + i);
            testTask.setStatus(TaskStatus.NEW);
            testTask.setDescription("Описание супер важной задачи: " + i);
            testTask.setUserId(8L);
            taskList.add(testTask);
        }
        return taskList;
    }

    private TaskRequestDto createTestRequestDto() {
        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle("Супер задача для RequestDto");
        dto.setStatus(TaskStatus.IN_PROGRESS);
        dto.setDescription("Описание задачи для RequestDto");
        dto.setUserId(12L);
        return dto;
    }
}