package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 29/04/16.
 */
public class TokenIsNotUniqueException extends MyDriveException {

    private static final long serialVersionUID = 1L;


    public TokenIsNotUniqueException() {
        super();
    }


    @Override
    public String getMessage() {
        return "This token has already been generated.";
    }
}
