package pt.tecnico.myDrive.exception;

public class CannotBeExecutedException extends MyDriveException {

    private static final long serialVersionUID = 1L;
    private String _name;

    public CannotBeExecutedException(String name) {
	_name = name;
    }

    public String getFileName(){
	return _name;
    }

    @Override
    public String getMessage() {
	return "The file " + _name + " cannot be executed";
    }
}
