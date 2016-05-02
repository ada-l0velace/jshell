package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 02/05/16.
 */
public class LoopedLinkException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public LoopedLinkException() {
        super();
    }

    @Override
    public String getMessage() {
        return "This link is in a loop!";
    }
}
