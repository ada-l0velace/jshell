package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 13/05/16.
 */
public class PasswordChangeException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _username;

    public PasswordChangeException(String username) {
        _username = username;
    }

    public String getUsername() {
        return _username;
    }

    @Override
    public String getMessage() {
        return "The password of the user: " + _username + " can not be changed.";
    }
}