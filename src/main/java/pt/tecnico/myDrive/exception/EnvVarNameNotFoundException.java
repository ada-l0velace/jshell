package pt.tecnico.myDrive.exception;

public class EnvVarNameNotFoundException extends MyDriveException {

    private static final long serialVersionUID = 1L;
    private String _name;

    public EnvVarNameNotFoundException(String name) {
    _name = name;
    }

    public String getName(){
    return _name;
    }

    @Override
    public String getMessage() {
    return "The name " + _name + " doesn't exist!";
    }
}
