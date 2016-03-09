package pt.tecnico.myDrive.exception;

public class NameFileAlreadyExistsException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String conflictingName;

    public NameFileAlreadyExistsException(String conflictingName) {
        conflictingName = conflictingName;
    }

    public String getConflictingName() {
        return conflictingName;
    }

    @Override
    public String getMessage() {
        return "This name " + conflictingName + " is already being used on this directory";
    }
}
