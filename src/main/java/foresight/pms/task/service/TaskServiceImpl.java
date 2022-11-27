package foresight.pms.task.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import foresight.pms.task.dao.TaskRepository;
import foresight.pms.task.dto.TaskDto;
import foresight.pms.task.dto.exceptions.EntityAddingErrorException;
import foresight.pms.task.dto.exceptions.EntityNotFoundException;
import foresight.pms.task.model.Task;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

	TaskRepository taskRepository;
	ModelMapper modelMapper;

	@Override
	@Transactional
	public boolean addTask(TaskDto taskDto) {
		String taskDtoParentUid = taskDto.getParentUid();
		Task parentTask = null;
		if (taskRepository.count() != 0) {
			if (taskDtoParentUid == null) {
				throw new EntityAddingErrorException();
			}
			parentTask = taskRepository.findById(taskDtoParentUid)
					.orElseThrow(() -> new EntityNotFoundException());
			if ("TASK".equals(parentTask.getType().toUpperCase())) { 
				throw new EntityAddingErrorException();
			}
		} else {
			if (taskDtoParentUid != null) {
				return false;
			}
		}
		if ("TASK".equals(taskDto.getType().toUpperCase())) {
			taskRepository.save(modelMapper.map(taskDto, Task.class));
		} else {
			Task task = new Task(taskDto.getName(), taskDto.getType(), taskDto.getStartDate(), taskDto.getEndDate(), taskDtoParentUid);
			taskRepository.save(task);
			Task childTask = new Task(taskDto.getName(), "TASK", LocalDate.now(), LocalDate.now(), task.getUid());
			taskRepository.save(childTask);
		}
		LocalDate[] projectDates = calculationOfTaskStartEndDates(taskDtoParentUid);
		parentTask.setStartDate(projectDates[0]);
		parentTask.setEndDate(projectDates[1]);
		taskRepository.save(parentTask);
		calculationOfProjectStartEndDates(taskDtoParentUid);
		return true;
	}

	private void calculationOfProjectStartEndDates(String id) {
		Task task = taskRepository.findById(id).orElse(null);
		while (task.getParentUid() != null) {
			LocalDate[] projectDates = calculationOfTaskStartEndDates(task.getParentUid());
			task.setStartDate(projectDates[0]);
			task.setEndDate(projectDates[1]);
			taskRepository.save(task);
		}
	}

	private LocalDate[] calculationOfTaskStartEndDates(String id) {
		List<String> uidList = new ArrayList<>();
		uidList = createListOfUid(id, uidList);

		List<LocalDate> startDateList = uidList.stream().map(u -> taskRepository.findById(u).orElse(null))
				.map(t -> t.getStartDate()).filter(d -> d != null).collect(Collectors.toList());
		List<LocalDate> endDateList = uidList.stream().map(u -> taskRepository.findById(u).orElse(null))
				.map(t -> t.getEndDate()).filter(d -> d != null).collect(Collectors.toList());
		return new LocalDate[] { Collections.min(startDateList), Collections.max(endDateList) };
	}

	@Override
	public TaskDto findTaskById(String uid) {
		Task task = taskRepository.findById(uid).orElseThrow(() -> new EntityNotFoundException());
		return modelMapper.map(task, TaskDto.class);
	}

	@Override
	@Transactional
	public boolean removeTask(String uid) {
		if (!taskRepository.existsById(uid)) {
			throw new EntityNotFoundException();
		}
		List<String> uidList = new ArrayList<>();
		uidList.add(uid);
		uidList = createListOfUid(uid, uidList);
		uidList.stream().forEach(u -> taskRepository.deleteById(u));
		return true;
	}

	private List<String> createListOfUid(String id, List<String> idList) {
		List<String> subIdList = taskRepository.findByParentUid(id).map(t -> t.getUid()).collect(Collectors.toList());
		if (subIdList.size() > 0) {
			idList.addAll(subIdList);
			subIdList.forEach(u -> createListOfUid(u, idList));
		}
		return idList;
	}

	@Override
	public long taskCompletionStatus(String uid, LocalDate date) {
		Task task = taskRepository.findById(uid).orElseThrow(() -> new EntityNotFoundException());
		return taskCompletionPercentage(date, task.getStartDate(), task.getEndDate());
	}

	private long taskCompletionPercentage(LocalDate date, LocalDate startDate, LocalDate endDate) {
		long generalPeriod = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		long elapsedPeriod = ChronoUnit.DAYS.between(startDate, date);
		return ((elapsedPeriod <= 0) ? 0 : (elapsedPeriod * 100 / generalPeriod));
	}

	@Override
	@Transactional
	public TaskDto updateTaskDate(String uid, LocalDate startDate, LocalDate endDate) {
		Task task = taskRepository.findById(uid).orElseThrow(() -> new EntityNotFoundException());
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		taskRepository.save(task);
		calculationOfProjectStartEndDates(task.getParentUid());
		return modelMapper.map(task, TaskDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TaskDto> findAllTasks() {
		return taskRepository.findAll().stream()
							.map(t -> modelMapper.map(t, TaskDto.class))
							.collect(Collectors.toList());
	}

}
