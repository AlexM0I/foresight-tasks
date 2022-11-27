package foresight.pms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import foresight.pms.task.dao.TaskRepository;

@SpringBootApplication
public class ForesightPmsApplication implements CommandLineRunner {
	
	TaskRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ForesightPmsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	}

}
