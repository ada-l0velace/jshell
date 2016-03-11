package pt.tecnico.myDrive.exception;

public class UsernameAlreadyExistsException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _conflictingUsername;

    public UsernameAlreadyExistsException(String conflictingUsername) {
        _conflictingUsername = conflictingUsername;
    }

    public String getConflictingUsername() {
        return _conflictingUsername;
    }

    @Override
    public String getMessage() {
        return "This username " + _conflictingUsername + " is already being used";
    }
}
