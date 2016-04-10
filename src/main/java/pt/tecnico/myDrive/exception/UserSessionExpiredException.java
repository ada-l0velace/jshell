package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 10/04/16.
 */
public class UserSessionExpiredException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private String _username;

    public UserSessionExpiredException(String username) {
        _username = username;
    }

    public String getUsername() {
        return _username;
    }

    @Override
    public String getMessage() {
        return "User " + getUsername() + " is not in session";
    }
}
