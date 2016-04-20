package pt.tecnico.myDrive.exception;

public class ExecutePermissionException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private String _name;
    private String _user;

    public ExecutePermissionException(String name, String user) {
        _name = name;
        _user = user;
    }

    public String getFileName() {
        return _name;
    }

    @Override
    public String getMessage() {
        return "The user " + _user+ " doesn't have permissions to execute" + getFileName() + " file";
    }
}
