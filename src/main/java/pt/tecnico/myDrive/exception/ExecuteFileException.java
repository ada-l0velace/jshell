package pt.tecnico.myDrive.exception;

public class ExecuteFileException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	
	public ExecuteFileException(){
	}

	@Override
	public String getMessage() {
		return "File cannot be executed";
	}
}
