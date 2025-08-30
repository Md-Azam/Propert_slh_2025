package Models;

import common.MonthlySummary;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class MonthlySummaryResponse {


		private boolean status;
		private MonthlySummary data;
		private String message;

}
