package Models;

import javax.persistence.Column;

public class UpdateBedDto {

	
	@Column
	 private int id;
	  
	    @Column
	    private double defaultRent;
	    @Column
	    private boolean ac;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public double getDefaultRent() {
			return defaultRent;
		}
		public void setDefaultRent(double defaultRent) {
			this.defaultRent = defaultRent;
		}
		public boolean isAc() {
			return ac;
		}
		public void setAc(boolean ac) {
			this.ac = ac;
		}
	    
}
