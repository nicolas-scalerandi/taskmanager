package com.nico.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nico.taskmanager.exception.TaskNotFoundException;
import com.nico.taskmanager.model.Task;
import com.nico.taskmanager.model.TaskStatus;
import com.nico.taskmanager.service.JwtService;
import com.nico.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(TaskController.class)
@WithMockUser
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getAllTasks_deberiaDevolverListaDeTareas() throws Exception {

        // 1. ARRANGE — preparamos las tareas falsas y el comportamiento del mock

        Task tarea1 = new Task();
        tarea1.setId(1L);
        tarea1.setTitle("Tarea uno");
        tarea1.setStatus(TaskStatus.PENDING);

        Task tarea2 = new Task();
        tarea2.setId(2L);
        tarea2.setTitle("Tarea dos");
        tarea2.setStatus(TaskStatus.DONE);

        when(taskService.getAllTasks()).thenReturn(List.of(tarea1, tarea2));

        // 2. ACT + ASSERT — simulamos la petición HTTP y comprobamos la respuesta

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Tarea uno"))
                .andExpect(jsonPath("$[1].status").value("DONE"));
    }

    @Test
    void getTaskById_deberiaDevolverTarea() throws Exception {

        Task tarea1 = new Task();
        tarea1.setId(3L);
        tarea1.setTitle("Tarea tres");
        tarea1.setStatus(TaskStatus.PENDING);

        when(taskService.getTaskById(3L)).thenReturn(tarea1);

        mockMvc.perform(get("/api/tasks/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tarea tres"))
                .andExpect(jsonPath("$.id").value(3));


    }

    @Test
    void getTaskById_deberiaDevolverTareaNoExiste() throws Exception {

        when(taskService.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());

    }

    @Test
    void createTask_deberiaCrearTarea() throws Exception {

        // 1. ARRANGE

        Task tareaAEnviar = new Task();
        tareaAEnviar.setTitle("Nueva tarea");
        tareaAEnviar.setStatus(TaskStatus.PENDING);
        // sin id — así llegaría del cliente, antes de guardarse

        Task tareaCreada = new Task();
        tareaCreada.setId(10L);
        tareaCreada.setTitle("Nueva tarea");
        tareaCreada.setStatus(TaskStatus.PENDING);
        // con id — lo que "devolvería" el service tras guardar

        when(taskService.createTask(any(Task.class))).thenReturn(tareaCreada);

        // 2. ACT + ASSERT

        mockMvc.perform(post("/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tareaAEnviar)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Nueva tarea"));
    }

    @Test
    void updateTask_deberiaModificarTarea() throws Exception {

        Task tareaDeEntrada = new Task();
        tareaDeEntrada.setId(20L);
        tareaDeEntrada.setTitle("Tarea Modificada");
        tareaDeEntrada.setStatus(TaskStatus.PENDING);

        Task tareaModificada = new Task();
        tareaModificada.setId(20L);
        tareaModificada.setTitle("Tarea Modificada");
        tareaModificada.setStatus(TaskStatus.PENDING);

        // SOLUCIÓN: Agregamos eq() al primer argumento
        when(taskService.updateTask(eq(20L), any(Task.class))).thenReturn(tareaModificada);

        mockMvc.perform(put("/api/tasks/20")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tareaDeEntrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.title").value("Tarea Modificada"));
    }

    @Test
    void updateTask_deberiaDevolverExcepcion() throws Exception {

        Task tareaDeEntrada = new Task();
        tareaDeEntrada.setId(99L);
        tareaDeEntrada.setTitle("Tarea Modificada");
        tareaDeEntrada.setStatus(TaskStatus.PENDING);

        // SOLUCIÓN: Agregamos eq() aquí también (y eliminamos el duplicado)
        when(taskService.updateTask(eq(99L), any(Task.class))).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(put("/api/tasks/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tareaDeEntrada)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_deberiaEliminarTarea() throws Exception {

        mockMvc.perform(delete("/api/tasks/20").with(csrf()))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteTask_deberiaDevolverExcepcion() throws Exception {

        doThrow(new TaskNotFoundException(99L)).when(taskService).deleteTask(99L);

        mockMvc.perform(delete("/api/tasks/99").with(csrf()))
                .andExpect(status().isNotFound());

    }

}