package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.interfaces.IElementXml;
import org.jdom2.Element;

/**
 * Identifies the current person that is working, creating or managing files.
 * Without this you can’t access all services.
 * @see IElementXml
 */
public class User extends User_Base implements IElementXml {
    
    private Element _xml;

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
        setPermissions(new Permissions());
    }

    /**
     * Get function for xml element.
     * @return Element (JDOM library type) which represents a user.
     */
    public Element getXml() {
        return _xml;
    }
    
    /**
     * Imports a User from persistent state (XML format),
     * building it again.
     * This method builds only User type objects.
     * @see Permissions
     */
    @Override
    public void importXml () {
        Element node = _xml;
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
     * Description
     */
    @Override
    public Element exportXml () {
		Element node = new Element("user");
		node.setAttribute("name", getName());
		node.setAttribute("username", getUsername());
		node.setAttribute("password", getPassword());

		Element perm = new Element("permissions");
		perm.setAttribute("umask", Short.toString(getPermissions().getUmask()));
		
		node.addContent(perm);
		
		return node;
	}

}
