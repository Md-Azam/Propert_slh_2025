package common;



public class ResourecNotFound extends RuntimeException{



   
    private String message ;
    private boolean status ;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public ResourecNotFound(String message, boolean status) {
        super();
        this.message = message;
        this.status = status;
    }
    public ResourecNotFound() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
}