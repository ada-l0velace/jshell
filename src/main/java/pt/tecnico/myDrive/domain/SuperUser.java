package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.SuperUserRemoveException;

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

    /**
     * Alternate constructor
     * @param m represents the manager.
     */
    public SuperUser(Manager m) {
        this();
        setManagerU(m);
        //init(ROOT_NAME, ROOT_USERNAME, "***", ROOT_UMASK, m);
        //m.createUser(this);
    }

    /**
     * Determines if is a super user
     * @return Boolean true if it is, false if not.
     */
    @Override
    public boolean isSuperUser() {
        return true;
    }

    public void initSu() {
        init(getName(),getUsername(),getPassword(), getPermissions().getUmask(), getManagerU());
    }

    /**
     * Overrides the method remove to deny removing the root.
     * @throws SuperUserRemoveException occurs when tries to remove the user.
     */
    @Override
    public void remove() throws SuperUserRemoveException {
        throw new SuperUserRemoveException(getUsername());
    }

}
