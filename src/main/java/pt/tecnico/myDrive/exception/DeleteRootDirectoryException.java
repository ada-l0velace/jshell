package pt.tecnico.myDrive.exception;

public class DeleteRootDirectoryException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _name;
    
    public DeleteRootDirectoryException(String name) {
		_name = name;
	}

	public String getInvalidName() {
		return _name;
	}

    @Override
    public String getMessage() {
    	return "You cannot delete " + _name + " directory";
    }
}
