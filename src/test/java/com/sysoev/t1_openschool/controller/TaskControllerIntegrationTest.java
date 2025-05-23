package com.sysoev.t1_openschool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysoev.t1_openschool.TestContainer;
import com.sysoev.t1_openschool.dto.TaskRequestDto;
import com.sysoev.t1_openschool.dto.TaskResponseDto;
import com.sysoev.t1_openschool.model.enums.TaskStatus;
import com.sysoev.t1_openschool.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest extends TestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private TaskResponseDto createTask(String title, String description, TaskStatus status, Long userId) throws Exception {
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setStatus(status);
        requestDto.setUserId(userId);

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, TaskResponseDto.class);
    }

    @Test
    @DisplayName("GET /tasks — успешный тест с пустой БД")
    void getAllTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /tasks/{id} — успешный поиск по ID")
    void getTaskById() throws Exception {
        TaskResponseDto task = createTask("Супер задача", "Описание задачи", TaskStatus.IN_PROGRESS, 99L);

        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Супер задача"))
                .andExpect(jsonPath("$.description").value("Описание задачи"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.userId").value(99));
    }

    @Test
    @DisplayName("GET /tasks/{id} — НЕуспешный поиск при ненайденной задаче")
    void getTaskById_notFound() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 99999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /tasks — успешное создание задачи")
    void createTask() throws Exception {
        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle("Новая задача");
        dto.setDescription("Создана через POST");
        dto.setStatus(TaskStatus.NEW);
        dto.setUserId(10L);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Новая задача"))
                .andExpect(jsonPath("$.description").value("Создана через POST"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.userId").value(10));
    }

    @Test
    @DisplayName("POST /tasks — НЕуспешное создание таски (пустые поля)")
    void createTask_emptyFields() throws Exception {
        String invalidJson = """
            {
              "title": ,
              "description":,
              "status":,
              "userId":
            }
        """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tasks — НЕуспешное создание таски (некорректный статус)")
    void createTask_invalidStatus() throws Exception {
        String invalidJson = """
            {
              "title": "Задача",
              "description": "Неверный статус",
              "status": "UNKNOWN",
              "userId": 1
            }
        """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /tasks/{id} — успешное обновление таски")
    void updateTask() throws Exception {
        TaskResponseDto created = createTask("Первичная", "Описание до обновления", TaskStatus.NEW, 22L);

        String updatedJson = """
                {
                    "title": "Обновлённая задача",
                    "description": "Описание после обновления",
                    "status": "DONE",
                    "userId": 22
                }
                """;

        mockMvc.perform(put("/tasks/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Обновлённая задача"))
                .andExpect(jsonPath("$.description").value("Описание после обновления"))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.userId").value(22));
    }

    @Test
    @DisplayName("DELETE /tasks/{id} — успешное удаление")
    void deleteTask() throws Exception {
        TaskResponseDto task = createTask("Удаляемая", "Для удаления", TaskStatus.NEW, 123L);

        mockMvc.perform(delete("/tasks/{id}", task.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isNotFound());
    }
}
