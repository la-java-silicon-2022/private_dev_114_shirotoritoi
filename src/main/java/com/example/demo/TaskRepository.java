package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
	List<Task> findByCompleted(Boolean completed);

	List<Task> findByCompletedAndShared(Boolean completed, Integer shared);

	List<Task> findByCompletedAndSharedAndNameOrderByDate(Boolean completed, Integer shared, String name);

	List<Task> findByShared(Integer shared);

	List<Task> findBySharedAndCompleted(Integer shared, Boolean completed);

	List<Task> findBySharedAndName(Integer shared, String name);

	List<Task> findByCompletedAndName(Boolean completed, String name);
}
