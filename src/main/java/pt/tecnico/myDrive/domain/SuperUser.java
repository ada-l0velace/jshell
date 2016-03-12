package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.User;



/**
 * Identifies the admin that as total access to all files.
 */
public class SuperUser extends SuperUser_Base {

	public static String ROOT_USERNAME = "root";
	public static String ROOT_NAME = "Super User";
	public static Short ROOT_UMASK = (short) Integer.parseInt("F2",16);
	
	/**
	 * Default Constructor
	 */
	public SuperUser() {
		super();
		setName(ROOT_NAME);
        setUsername(ROOT_USERNAME);
        setPassword("***");
        setPermissions(new Permissions(ROOT_UMASK));
        // Manager.log.trace(Manager.getInstance().getHome());
        //setHome();
	}

    /**
     * Inserts on SuperUser home directory a link to /home
     * @param managerHome (Directory) which represents a directory where all Users will be.
     * @param m           (Manager) represents the Manager.
     */
	public void setHome(Directory managerHome, Manager m) {		
        super.setHome(new Directory(this, getUsername(), new Link ("..", managerHome,"/home",m),m));
    }
}
