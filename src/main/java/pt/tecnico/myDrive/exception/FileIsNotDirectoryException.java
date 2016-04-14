package pt.tecnico.myDrive.exception;

public class FileIsNotDirectoryException extends MyDriveException {

    private static final long serialVersionUID = 1L;
    private String _filename;
    
    public FileIsNotDirectoryException(String filename){
        _filename = filename;
    }

    public String getFileName() {
        return _filename;
    }

    @Override
    public String getMessage(){
        return "File " + _filename + " is not a directory";
    }
}
