package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 13/05/16.
 */
public class EnvironmentVariableAlreadyExists extends MyDriveException {

    private static final long serialVersionUID = 1L;
    private String _name;

    public EnvironmentVariableAlreadyExists(String name) {
        _name = name;
    }

    public String getFileName(){
        return _name;
    }

    @Override
    public String getMessage() {
        return "The variable " + _name + " already exists.";
    }
}
