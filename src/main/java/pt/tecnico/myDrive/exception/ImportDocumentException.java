package pt.tecnico.myDrive.exception;

public class ImportDocumentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String _name;
    
    public ImportDocumentException(String name) {
        _name = name;
     }

    public String getInvalidName() {
        return _name;
     }

   @Override
   public String getMessage() {
       return "Error in importing " + _name + "from XML";
    }
}