package Models;

import common.MonthlySummary;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response<T> {

	private boolean status;
	private T data;
	private String message;

}
