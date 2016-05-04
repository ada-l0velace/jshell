package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 04/05/16.
 * Exec format error
 */
public class ExecFormatErrorException extends MyDriveException {
    private static final long serialVersionUID = 1L;
    private String _name;

    public ExecFormatErrorException(String name) {
        _name = name;
    }

    public String getFileName(){
        return _name;
    }

    @Override
    public String getMessage() {
        return "The file '" + _name + "' is marked as an executable but could not be run by the operating system.";
    }
}
