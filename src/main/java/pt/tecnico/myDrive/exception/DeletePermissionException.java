package pt.tecnico.myDrive.exception;

public class DeletePermissionException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _filename;
    private String _username;

    public DeletePermissionException(String filename, String username) {
        _filename = filename;
        _username = username;
    }

    public String getFilename() {return _filename;}
    public String getUsername() {return _username;}

    @Override
    public String getMessage() {
        return "The user: " + getUsername() + "doesn't have permission to delete " + getFilename();
    }
}