package foresight.pms.task.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import foresight.pms.task.model.Task;

public interface TaskRepository extends JpaRepository<Task, String> {

	Stream<Task> findByParentUid(String id);

}
