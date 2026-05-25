package com.nico.taskmanager.service;

import com.nico.taskmanager.exception.TaskNotFoundException;
import com.nico.taskmanager.model.Task;
import com.nico.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, Task updatedTask){
        Task tempTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        tempTask.setTitle(updatedTask.getTitle());
        tempTask.setDescription(updatedTask.getDescription());
        tempTask.setStatus(updatedTask.getStatus());

        return taskRepository.save(tempTask);

    }
}
