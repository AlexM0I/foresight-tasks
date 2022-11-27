package foresight.pms.task.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class EntityAddingErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6634408658955081791L;
}
