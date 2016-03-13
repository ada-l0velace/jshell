package pt.tecnico.myDrive.exception;

public class FileIdNotFoundException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private int _id;

    public FileIdNotFoundException(int id) {
        _id = id;
    }

    public int getFileId() {
        return _id;
    }

    @Override
    public String getMessage() {
        return "File id " + _id + " does not exist";
    }
}