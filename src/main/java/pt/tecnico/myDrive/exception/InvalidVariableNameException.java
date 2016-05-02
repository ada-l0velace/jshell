package pt.tecnico.myDrive.exception;

public class InvalidVariableNameException extends MyDriveException {

    private static final long serialVersionUID = 1L;
    private String _name;

    public InvalidVariableNameException(String name) {
	_name = name;
    }

    public String getFileName(){
	return _name;
    }

    @Override
    public String getMessage() {
	return "The variable " + _name + " cannot have that name";
    }
}
