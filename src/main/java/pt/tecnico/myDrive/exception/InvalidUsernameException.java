package pt.tecnico.myDrive.exception;


public class InvalidUsernameException extends MyDriveException {
    
     private static final long serialVersionUID = 1L;
     
     private String _username;

     public InvalidUsernameException(String username) {
         _username = username;
     }

     public String getInvalidUsername() {
         return _username;
     }

    @Override
    public String getMessage() {
        return "Username " + _username + " is not valid";
    }
}
