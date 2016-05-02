package pt.tecnico.myDrive.exception;

public class EmptyVariableValueException extends MyDriveException {

    private static final long serialVersionUID = 1L;
    private String _name;

    public EmptyVariableValueException(String name) {
	_name = name;
    }

    public String getFileName(){
	return _name;
    }

    @Override
    public String getMessage() {
	return "The variable content " + _name + " is empty";
    }
}
