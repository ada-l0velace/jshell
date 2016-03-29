package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 29/03/16.
 */
public class SuperUserRemoveException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _username;

    public SuperUserRemoveException(String username) {
        _username = username;
    }

    public String getUsername() {
        return _username;
    }

    @Override
    public String getMessage() {
        return "The user " + _username + " is a super user and can not be deleted.";
    }
}

