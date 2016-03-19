package pt.tecnico.myDrive.exception;

public class WritePermissionException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private String _name;

    public WritePermissionException(String name) {
        _name = name;
    }

    public String getFileName() {
        return _name;
    }

    @Override
    public String getMessage() {
        return "You don't have permissions to write on " + getFileName() + " file";
    }
}