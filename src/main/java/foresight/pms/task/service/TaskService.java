package foresight.pms.task.service;

import java.time.LocalDate;

import foresight.pms.task.dto.TaskDto;

public interface TaskService {
	boolean addTask(TaskDto taskDto);
	
	TaskDto findTaskById(String id);
	
	boolean removeTask(String id);
	
	long taskCompletionStatus(String id, LocalDate date);
	
	TaskDto updateTaskDate(String id, LocalDate startDate, LocalDate endDate);

	Iterable<TaskDto> findAllTasks();

}
