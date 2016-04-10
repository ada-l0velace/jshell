package pt.tecnico.myDrive.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.WritePermissionException;

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
     * Alternative constructor to create a user.
     * @param name (String) represents the real name.
     * @param username (String) represents the login username.
     * @param password (String) represents the login password.
     * @param umask (Short) represents the permission umask.
     */
    public User(String name, String username, String password, Short umask, Manager m) {
        super();
        //super.setManagerU(m);
        init(name, username, password, umask, m);
        //m.createUser(this);
    }
    
    /**
     * Protected constructor to init a user.
     * @param name     (String) represents the real name.
     * @param username (String) represents the username.
     * @param password (String) represents the password.
     * @param umask    (Short)  represents the permissions umask.
     */
    protected void init(String name, String username, String password, Short umask, Manager m) {
        setName(name);
        setUsername(username);
        setPassword(password);
        setPermissions(new Permissions(umask));
        setManagerU(m);
        initHome();
    }

    /**
     * Initiate directory home at default location.
     */
    protected void initHome() {
        Directory home = new Directory(this, getUsername(), getManagerU().getDirHome(), getManagerU());
        setHome(home);
    }
    
    /**
     * Alternate constructor to create a user with xml
     * @param xml (Element JDOM) represents a User in xml format.
     */
    public User(Element xml) {
        super();
        importXml(xml);
    }

    /**
     * Overrides setName for checking username validation.
     * @param username (String) represents a name for the file.
     * @throws InvalidUsernameException occurs when a file name contains other than letters and numbers.
     * */
    @Override
    public void setUsername(String username) throws InvalidUsernameException {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches() || username.length()<3)
            throw new InvalidUsernameException(username);
        else
            super.setUsername(username);
    }

    /**
     * Determines if is a super user
     * @return Boolean true if it is, false if not.
     */
    public boolean isSuperUser() {
        return false;
    }

    /**
     * Imports a User from persistent state (XML format).
     * @param xml (Element JDOM) represents a User in xml format.
     * @see Permissions
     * @throws ImportDocumentException
     */
    public void importXml (Element xml) {
        Element node = xml;
        String name = node.getAttribute("name").getValue();
        String username = node.getAttribute("username").getValue();
        String password = node.getAttribute("password").getValue();
        short umask = Short.parseShort(node.getAttribute("umask").getValue());
        Directory home;
        if (getHome() != null) {
            Directory h = getHome();
            setHome(null);
            h.remove();
        }
        setName(new String(name));
        setUsername(new String(username));
        setPassword(new String(password));
        setPermissions(new Permissions(umask));
        Element dir = xml.getChild("Directory");
        home = new Directory(dir, this);
        setHome(home);
    }

    /**
     * Creates a link to a File.
     * @param file (File) represents the file to Link.
     * @param name (String) represents the name of the Link.
     * @param p (Directory) represents the parent directory where the link is inserted.
     * @param m (Manager) represents the application manager.
     * @return Link returns the Link created.
     */
    public Link createLink(File file, String name, Directory p, Manager m) {
        String l = file.getPath();
        return new Link (name, file, l + file.getName(), p, m);
    }

    /**
     * Modifies a file with a link.
     * @param link represents the path to the file.
     * @param file represents the file to add to the directory.
     * @throws WritePermissionException occurs when the user doesn't have permissions to write on this directory.
     */
    public void createFile(Directory parent, File file) throws WritePermissionException {
        if (getUsername() == parent.getOwner().getUsername())
            parent.addFile(file);
        else {
            if (parent.getPermissions().worldCanWrite())
                parent.addFile(file);
            else
                throw new WritePermissionException(parent.getName());
        }
    }

    /**
     * Modifies a file with a link
     * @param link represents the path to the file.
     * @param content represents
     */
    /*
    public void modifyFile(Link link, String content) throws WritePermissionException, FileNotFoundException {
        File file = getFileByPath(link.getContent());
        if (getUsername() == link.getOwner().getUsername())
            file.setContent(content);
        else {
            if (link.getPermissions().worldCanWrite())
                file.setContent(content);
            else
                throw new WritePermissionException(file.getName());
        }
    }
    */

    /**
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Element xmlExport(){
        Element node = new Element("User");
        node.setAttribute("username", getUsername());
        node.setAttribute("name", getName());
        node.setAttribute("password", getPassword());
        node.setAttribute("umask", Short.toString(getPermissions().getUmask()));
        return node;
    }

    /**
     * Exports a User to a persistent state (XML format),
     * @see Permissions
     * @return Element (JDOM library type) which represents a User
     */
    public Element exportXml () {
        Element node = new Element("User");
        node.setAttribute("username", getUsername());
        
        Element name = new Element("name");
        name.addContent(getName());
        Element password = new Element("password");
        password.addContent(getPassword());
        Element umask = new Element("mask");
        umask.addContent(Short.toString(getPermissions().getUmask()));
        Element home = new Element("home");
        home.addContent(getHome().getPath() + getHome().getName());
        
        
        node.addContent(name);
        node.addContent(password);
        node.addContent(umask);
        node.addContent(home);
        
        return node;
    }

    /**
     * Gets the file by a path.
     * @// FIXME: 18/03/16 needs refactoring.
     * @param  link (String) receives a String with the link content.
     * @return  File returns the last File that appears in the path.
     */
    public File getFileByPath (String link) throws FileNotFoundException {
        if ( (link.charAt(link.length() - 1)== '/') && (link.length() > 1)){
        		link = link.substring(0, link.length() -1);
    	}
        if (link.equals("session")){
        	return getSession().getCurrentDirectory();
        }
        String[] split = link.split("/",2);
        String init = split[0];
    	String rest = split[1];
        if(link.startsWith("/")){
            	return Manager.getInstance().getHome().getFileByPath(rest);
        }
        else if (link.startsWith("session/")){
            	return getSession().getCurrentDirectory().getFileByPath(rest);
        }
        throw new FileNotFoundException(link);
    }

    /**
     * Overriding set manager.
     * @param m Application Manager.
     */
    @Override
    public void setManagerU(Manager m) {
        if (m == null)
            remove();
        else {
            //super.setManagerU(m);
            m.createUser(this);
        }
    }

    /**
     * Gets a File by a path.
     * @param  link (Link) receives a link to a plain file.
     * @return  String returns the string with the File content.
     */
    public String getFileContentByLink(Link link){     
        File pf = getFileByPath(link.getContent());
        return pf.getContent();
    }

    /**
     * Delete a file.
     * @param link (Link) represents the link to the file or empty directory.
     */
    public void deleteFile(Link link) {
        File to_delete  = getFileByPath(link.getContent());
        to_delete.remove();
    }

    /**
     * Removes User and related objects.
     * @// FIXME: 18/03/16 doesn't work because needs to setHome(null); before the loop.
     */
    public void remove() {
        super.setManagerU(null);
        for(File i : getFileSet()) {
            i.remove();
        }
        setHome(null);
        setPermissions(null);
        deleteDomainObject();
    }
}
