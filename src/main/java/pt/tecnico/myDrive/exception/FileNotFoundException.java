package pt.tecnico.myDrive.exception;

public class FileNotFoundException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _fileName;

    public FileNotFoundException(String fileName) {
        _fileName = fileName;
    }

    public String getFileName() {
        return _fileName;
    }

    @Override
    public String getMessage() {
        return "File " + _fileName + " does not exist";
    }
}