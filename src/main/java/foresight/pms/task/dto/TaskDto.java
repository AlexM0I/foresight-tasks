package foresight.pms.task.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class TaskDto {
	String name;
	String type;
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate startDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate endDate;
	String parentUid;
}
