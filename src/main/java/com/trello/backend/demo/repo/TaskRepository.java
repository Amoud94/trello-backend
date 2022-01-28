package com.trello.backend.demo.repo;

import com.trello.backend.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    Task findByTaskID(String taskId);
    Collection<Task> findTasksByBoardList_BoardListID(String boardListId);
}
