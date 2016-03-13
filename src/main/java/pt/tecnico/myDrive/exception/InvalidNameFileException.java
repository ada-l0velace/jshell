package pt.tecnico.myDrive.exception;


public class InvalidNameFileException extends MyDriveException {
    
     private static final long serialVersionUID = 1L;
     
     private String _name;

     public InvalidNameFileException(String name) {
         _name = name;
     }

     public String getInvalidName() {
         return _name;
     }

    @Override
    public String getMessage() {
        return "Name " + _name + " is not valid";
    }
}
