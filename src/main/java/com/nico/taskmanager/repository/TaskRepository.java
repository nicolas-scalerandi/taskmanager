package com.nico.taskmanager.repository;

import com.nico.taskmanager.model.Task;
import com.nico.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

}
