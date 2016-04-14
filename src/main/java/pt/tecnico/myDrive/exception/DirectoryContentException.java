package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 14/04/16.
 */
public class DirectoryContentException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public DirectoryContentException() {
        super();
    }

    @Override
    public String getMessage() {
        return "You cannot create a directory with content!";
    }
}
