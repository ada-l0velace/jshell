package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.interfaces.IElementXml;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom2.Element;
import pt.tecnico.myDrive.domain.Directory;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.FileNotFoundException;


/**
 * Identifies the current person that is working, creating or managing files.
 * Without this you cannot access all services.
 */
public class User extends User_Base {
    
	/**
	 * Default Constructor
	 */
	public User() {
		super();
	}

	/**
	 * Alternative construtor to create a user.
	 * @param name String (Primary java type) represents the real name.
	 * @param username String (Primary java type) represents the login username.
	 * @param password String (Primary java type) represents the login password.
	 * @param umasks Short (Primary java type) represents the permission umask.
	 */
	public User(String name, String username, String password, Short umask, Manager m) {
		super();
        init(name, username, password, umask, m);
	}
	
    /**
     * Protected constructor to init a user.
     * @param name     String (Primary java type) represents the real name.
     * @param username String (Primary java type) represents the username. 
     * @param password String (Primary java type) represents the password.
     * @param umask    Int (Primary java type) represents the permissions umask.
     */
	protected void init(String name, String username, String password, Short umask, Manager m) {
        setName(name);
        setUsername(username);
        setPassword(password);
        setPermissions(new Permissions(umask));
        // Manager.log.trace(Manager.getInstance().getHome());
        setHome(new Directory(this, username, new Link ("..", m.getDirHome(),"/home", m),m));
    }
	
	/**
	 * Alternate construtor to create a user with xml
	 * @param Element (JDOM library type) which represents a User
	 */
	public User(Element xml) {
        super();
		importXml(xml);
	}
	
	/**
	 * Do override of setUsername checking for special caracters
	 * @param String (JavaPrimitive) which represents a username
	 */
	@Override
    public void setUsername(String username) throws InvalidUsernameException {
		/*Pattern from "a" to "z" or from "A" to "Z" or from "0" to "10"*/
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
		/*Check if string username macth the indicated pattern*/
		Matcher matcher = pattern.matcher(username);
		/*If it doesnt macth thorws InvalidUsernameException*/
		if (!matcher.matches()) {
			throw new InvalidUsernameException(username);
		}
		/*If username macth does set to username*/
		else{
			super.setUsername(username);
		}
    }

 	
    /**
     * Imports a User from persistent state (XML format).
     * @param Element (JDOM library type) which represents a User
     * @see Permissions
     * @throws ImportDocumentException
     */
    public void importXml (Element xml) {
        for(File f : getFileSet())
        {
            f.remove();
        }
        Element node = xml;
        String name = node.getAttribute("name").getValue();
        String username = node.getAttribute("username").getValue();
        String password = node.getAttribute("password").getValue();
        short umask = Short.parseShort(node.getAttribute("umask").getValue());
        Directory home;
        Manager.log.trace(name);
        setName(new String(name));
        setUsername(new String(username));
        setPassword(new String(password));
        setPermissions(new Permissions(umask));
        for (Element dir: xml.getChildren("Directory")) {
            Manager.log.trace("DIRECTORY INNN" + dir);
            home = new Directory(dir);
            setHome(home);
        }
    }

	/**
	 * Exports a User to a persistent state (XML format),
	 * @see Permissions
	 * @return Element (JDOM library type) which represents a User
	 */
	public Element exportXml () {
		Element node = new Element("User");
		node.setAttribute("name", getName());
		node.setAttribute("username", getUsername());
		node.setAttribute("password", getPassword());
		node.setAttribute("umask", Short.toString(getPermissions().getUmask()));

		node.addContent(getHome().exportXml());
		
		return node;
	}

    /**
     * Delete a file or empty directory,
     * @param String link represents the link to the file or empty directory.
     */
    public void deleteFileOrEmptyDirectory(String link)
    {
        File to_delete  = getFileByPath(link);
        to_delete.remove();
	}

    /**
    * 
    * @param  String link receives a String with the link content. 
    * @return  File  returns the last File that appears in the path.
    */
    public File getFileByPath (String link) throws FileNotFoundException {
    	String[] spliTest = link.split("/");
    	if (spliTest.length == 1){
    		if(link.equals("home")){
    		return getHome();
    		}
    	}
    	String[] split = link.split(Pattern.quote("/"),2);
    	String rest = split[1];
    	String nomeInit = split[0];
    	if(nomeInit.equals("home")){
    		return getHome().getFileByPath(rest);
    	}
    	throw new FileNotFoundException(nomeInit);
    }
    
    /**
     * @param  Link link receives a link to a plain file. 
     * @return  String returns the string with the plain file content.
     */

    public String getPFileContentByLink(Link link){ 	
    	return getFileByPath(link.getContent()).toString();
    }
    
    /**
     * Get a list of the file names inside of the directory
     * @param Link link represents the path to the directory.
     */
    public String getDirContentByLink(Link link){
    	Directory Dir = (Directory)getFileByPath(link.getContent());
    	return Dir.listContent();
    }

    
    public void remove()
    {
        for(File i : getFileSet())
        {
            i.remove();
        }
        setHome(null);
        setPermissions(null);
        deleteDomainObject();
    }
}
