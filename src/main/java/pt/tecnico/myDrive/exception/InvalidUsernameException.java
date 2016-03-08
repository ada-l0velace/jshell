package pt.tecnico.myDrive.exception;

public class InvalidUsernameException extends MyDriveException {
	
	 private static final long serialVersionUID = 1L;
	 
	 private String username;

	 public InvalidUsernameException(String name) {
		 username = username;
	 }

	 public String getInvalidUsername() {
		 return username;
	 }

    @Override
    public String getMessage() {
    	return "Username " + username + " is not valid";
    }
}
