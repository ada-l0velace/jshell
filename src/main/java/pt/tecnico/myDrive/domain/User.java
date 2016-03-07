package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.interfaces.IElementXml;
import org.jdom2.Element;


/**
 * Identifies the current person that is working, creating or managing files.
 * Without this you can’t access all services.
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
	public User(String name, String username, String password, Short umask) {
		super();
		setName(name);
		setPassword(password);
		setPermissions(new Permissions(umask));
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
     * Imports a User from persistent state (XML format).
     * @param Element (JDOM library type) which represents a User
     * @see Permissions
     * @throws ImportDocumentException
     */
    public void importXml (Element xml) {
        Element node = xml;
        String name = node.getAttribute("name").getValue();
        String username = node.getAttribute("username").getValue();
        String password = node.getAttribute("password").getValue();
        Element permission = node.getChild("permissions");
        short umask = Short.parseShort(permission.getAttribute("umask").getValue());
        
		setName(new String(name));
		setUsername(new String(username));
		setPassword(new String(password));
		setPermissions(new Permissions(umask));
	}

	/**
	 * Exports a User to a persistent state (XML format),
	 * @see Permissions
	 * @return Element (JDOM library type) which represents a User
	 */
	public Element exportXml () {
		Element node = new Element("user");
		node.setAttribute("name", getName());
		node.setAttribute("username", getUsername());
		node.setAttribute("password", getPassword());

		Element perm = new Element("permissions");
		perm.setAttribute("umask", Short.toString(getPermissions().getUmask()));

		node.addContent(perm);
		node.addContent(getHome().exportXml());
		
		return node;
	}
    
    public File getFileByPath (String link){
		String[] split = link.split("/",2);
		String rest = split[1];
		String nomeInit = split[0];
		if(nomeInit.equals("home")){
			return getHome().getFileByPath(rest);
		}
		else{
			throw new UnsupportedOperationException("Not Implemented!");
		}
	}
    
	public String getContentByLink(Link link){
		PlainFile fileToRead = (PlainFile)getFileByPath(link.getContent());
		return fileToRead.getContent();
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

}
