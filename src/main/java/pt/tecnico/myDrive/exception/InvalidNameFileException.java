package pt.tecnico.myDrive.exception;

public class InvalidNameFileException extends MyDriveException {
	
	 private static final long serialVersionUID = 1L;
	 
	 private String name;

	 public InvalidNameFileException(String name) {
		 name = name;
	 }

	 public String getInvalidName() {
		 return name;
	 }

    @Override
    public String getMessage() {
    	return "Name " + name + " is not valid";
    }
}
