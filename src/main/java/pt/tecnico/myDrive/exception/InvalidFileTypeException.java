package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 10/04/16.
 */
public class InvalidFileTypeException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private String _name;

    public InvalidFileTypeException(String name) {
        _name = name;
    }

    public String getInvalidFileType() {
        return _name;
    }

    @Override
    public String getMessage() {
        return "File type " + _name + " is not valid";
    }
}
