package foresight.pms.task.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = { "uid" })
@Entity
public class Task {
	@Id
	String uid;
	String name;
	String type;
	@Setter
	LocalDate startDate;
	@Setter
	LocalDate endDate;
	String parentUid;

	public Task() {
		this.uid = UUID.randomUUID().toString();
	}

	public Task(String name, String type, LocalDate startDate, LocalDate endDate, String parentUid) {
		this();
		this.name = name;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.parentUid = parentUid;
	}
}
