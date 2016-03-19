package pt.tecnico.myDrive.domain;

/**
 * Identifies the admin that as total access to all files.
 */
public class SuperUser extends SuperUser_Base {

    public static String ROOT_USERNAME = "root";
    public static String ROOT_NAME = "Super User";
    public static Short ROOT_UMASK = (short) Integer.parseInt("FA",16);
    
    /**
     * Default Constructor
     */
    public SuperUser() {
        super();
        setName(ROOT_NAME);
        setUsername(ROOT_USERNAME);
        setPassword("***");
        setPermissions(new Permissions(ROOT_UMASK));
    }
    public SuperUser(Manager m) {
        this();
        setManagerSu(m);
        setManagerU(m);
    }

}
