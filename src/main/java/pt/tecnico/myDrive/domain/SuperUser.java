package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.User;



/**
 * Identifies the admin that as total access to all files.
 */
public class SuperUser extends SuperUser_Base {
	/*Defines the properaties of the SuperUser
	 * its username, name, directory and mask
	 * */
	
	public static String ROOT_USERNAME = "root";
	public static String ROOT_NAME = "Super User";
	public static String ROOT_DIRECTORY = "/home/root";
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

	@Override
	public void setHome(Directory managerHome) {		
		super.setHome(new Directory(this, getName(), new Link ("..", managerHome)));
	}
}
