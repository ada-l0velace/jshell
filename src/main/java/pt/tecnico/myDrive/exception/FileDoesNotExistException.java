package pt.tecnico.myDrive.exception;

public class FileDoesNotExistException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String fileName;

    public FileDoesNotExistException(String fileName) {
        fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getMessage() {
        return "File " + fileName + " does not exist";
    }
}