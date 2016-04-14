package pt.tecnico.myDrive.exception;

public class SpecialDirectoriesException extends MyDriveException { 

    private static final long serialVersionUID = 1L;

    private String _username;

    public SpecialDirectoriesException( String username) {
        _username = username;
    }

    public String getUsername() {return _username;}

    @Override
    public String getMessage() {
        return "Refusing to remove . and .. directories " ;
    }
}