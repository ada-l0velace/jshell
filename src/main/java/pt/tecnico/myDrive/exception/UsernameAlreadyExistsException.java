package pt.tecnico.myDrive.exception;

public class UsernameAlreadyExistsException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String conflictingUsername;

    public UsernameAlreadyExistsException(String conflictingUsername) {
        conflictingUsername = conflictingUsername;
    }

    public String getConflictingUsername() {
        return conflictingUsername;
    }

    @Override
    public String getMessage() {
        return "This username " + conflictingUsername + " is already being used";
    }
}
