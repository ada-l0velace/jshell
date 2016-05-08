package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 05/05/16.
 */
public class InvalidPasswordException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {}

    @Override
    public String getMessage() {
        return "Password must have at least 8 characters.";
    }
}
