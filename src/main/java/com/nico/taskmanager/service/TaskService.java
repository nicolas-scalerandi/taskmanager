package com.nico.taskmanager.service;

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

    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask){
        Optional<Task> tempTask = taskRepository.findById(id);
        if(!tempTask.isEmpty()){
            Task task = tempTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
            Task saved = taskRepository.save(task);
            return Optional.of(saved);
        }else{
            return Optional.empty();
        }



    }
}
