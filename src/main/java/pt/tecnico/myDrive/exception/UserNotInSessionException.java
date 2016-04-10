package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 10/04/16.
 */
public class UserNotInSessionException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private String _token;

    public UserNotInSessionException(String token) {
        _token = token;
    }

    public String getToken() {
        return _token;
    }

    @Override
    public String getMessage() {
        return "Token " + getToken() + " is invalid";
    }
}
