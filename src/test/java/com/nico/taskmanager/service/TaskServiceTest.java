package com.nico.taskmanager.service;

import com.nico.taskmanager.exception.TaskNotFoundException;
import com.nico.taskmanager.model.Task;
import com.nico.taskmanager.model.TaskStatus;
import com.nico.taskmanager.model.User;
import com.nico.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskById_deberiaDevolverTarea_cuandoExiste() {
        // Arrange: preparamos la tarea falsa y le decimos al mock qué devolver
        Task tareaFalsa = new Task();
        tareaFalsa.setId(1L);
        tareaFalsa.setTitle("Aprender Mockito");
        tareaFalsa.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(tareaFalsa));

        // Act: llamamos al método real que estamos testeando
        Task resultado = taskService.getTaskById(1L);

        // Assert: comprobamos que el resultado es el esperado
        assertThat(resultado.getTitle()).isEqualTo("Aprender Mockito");
        assertThat(resultado.getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    void getTaskById_deberiaLanzarExcepcion_cuandoNoExiste() {
        // Arrange: el mock simula que no encuentra nada
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert: aquí van juntos porque estamos comprobando que algo lanza excepción
        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask_deberiaGuardarTarea() {

        // 1. ARRANGE — preparar los datos y el comportamiento del mock

        User usuarioFalso = new User();
        usuarioFalso.setId(1L);
        usuarioFalso.setEmail("test@test.com");

        Task tareaDeEntrada = new Task();
        tareaDeEntrada.setTitle("Test Guardar Tarea");
        tareaDeEntrada.setStatus(TaskStatus.PENDING);


        Task tareaGuardada = new Task();
        tareaGuardada.setId(2L);
        tareaGuardada.setTitle("Test Guardar Tarea");
        tareaGuardada.setStatus(TaskStatus.PENDING);
        // esta simula lo que "devolvería" la base de datos, con id

        when(taskRepository.save(any(Task.class))).thenReturn(tareaGuardada);


        // 2. ACT — llamar al método real que quieres testear

        Task resultado = taskService.createTask(tareaDeEntrada, usuarioFalso);


        // 3. ASSERT — comprobar que el resultado es el esperado

        assertThat(resultado.getId()).isEqualTo(2L);
        assertThat(resultado.getTitle()).isEqualTo("Test Guardar Tarea");
        assertThat(resultado.getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    void updateTask_deberiaModificarTarea(){

        Task tareaOriginal = new Task();
        tareaOriginal.setId(3L);
        tareaOriginal.setTitle("Test Tarea Sin Modificar");
        tareaOriginal.setStatus(TaskStatus.PENDING);

        Task tareaDeEntrada = new Task();
        tareaDeEntrada.setId(3L);
        tareaDeEntrada.setTitle("Test Tarea Modificada");
        tareaDeEntrada.setStatus(TaskStatus.PENDING);

        Task tareaModificada = new Task();
        tareaModificada.setId(3L);
        tareaModificada.setTitle("Test Tarea Modificada");
        tareaModificada.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(3L)).thenReturn(Optional.of(tareaOriginal));
        when(taskRepository.save(any(Task.class))).thenReturn(tareaModificada);

        Task resultado = taskService.updateTask(3L, tareaDeEntrada);

        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getTitle()).isEqualTo("Test Tarea Modificada");
        assertThat(resultado.getStatus()).isEqualTo(TaskStatus.PENDING);

    }

    @Test
    void deleteTask_deberiaEliminarTarea(){

        Task tareaOriginal = new Task();
        tareaOriginal.setId(4L);
        tareaOriginal.setTitle("Test Tarea A Eliminar");
        tareaOriginal.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(4L)).thenReturn(Optional.of(tareaOriginal));

        taskService.deleteTask(4L);

        verify(taskRepository).deleteById(4L);

    }
}