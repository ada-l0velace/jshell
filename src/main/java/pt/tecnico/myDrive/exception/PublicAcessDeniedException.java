package pt.tecnico.myDrive.exception;

/**
 * Created by lolstorm on 14/04/16.
 */
public class PublicAcessDeniedException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private String _name;
    private String _publicMethodToUser;

    public PublicAcessDeniedException(String name, String publicMethodToUser) {
        _name = name;
        _publicMethodToUser = publicMethodToUser;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage() {
        return "The method " + _name + " is protected against public access use " +_publicMethodToUser +" instead.";
    }
}
