package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 10/04/16.
 */
public class InvalidUserCredentialsException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public InvalidUserCredentialsException() {

    }

    @Override
    public String getMessage() {
        return "Username or password invalid.";
    }
}
