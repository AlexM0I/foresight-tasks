package foresight.pms.task.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import foresight.pms.task.dto.TaskDto;
import foresight.pms.task.service.TaskService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {

	TaskService taskService;

	@PostMapping
	public boolean addTask(@RequestBody TaskDto taskDto) {
		return taskService.addTask(taskDto);
	}

	@GetMapping("/{id}")
	public TaskDto findTask(@PathVariable String id) {
		return taskService.findTaskById(id);
	}

	@DeleteMapping("/{id}")
	public boolean removeTask(@PathVariable String id) {
		return taskService.removeTask(id);
	}

	@PutMapping("/{id}/date/{date}")
	public long taskCompletionStatus(@PathVariable String id, @PathVariable LocalDate date) {
		return taskService.taskCompletionStatus(id, date);
	}

	@PutMapping("/{id}/startDate/{startDate}/endDate/{endDate}")
	public TaskDto updateTaskDate(@PathVariable String id, @PathVariable LocalDate startDate,
			@PathVariable LocalDate endDate) {
		return taskService.updateTaskDate(id, startDate, endDate);
	}

	@GetMapping("project")
	public Iterable<TaskDto> findAllTasks() {
		return taskService.findAllTasks();
	}
}
