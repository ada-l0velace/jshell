package pt.tecnico.myDrive.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.WritePermissionException;
import pt.tecnico.myDrive.exception.InvalidNameFileException;
import pt.tecnico.myDrive.exception.ReadPermissionException;

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
        initHome();
        //m.createUser(this);
    }

    /**
     * Alternative constructor to create a user.
     * @param name (String) represents the real name.
     * @param username (String) represents the login username.
     * @param password (String) represents the login password.
     * @param umask (Short) represents the permission umask.
     * @param pHomePath (String) represents the parent home path.
     */
    public User(String name, String username, String password, Short umask, String pHomePath, Manager m) {
        super();
        //super.setManagerU(m);
        init(name, username, password, umask, m);
        initHome(pHomePath);
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

    }

    /**
     * Initiate directory home at default location.
     */
    protected void initHome() {
        User su = getManagerU().getSuperuser();
        Directory home;
        if(su == null)
            home = new Directory(this, getUsername(), getManagerU().getDirHome(), getManagerU());
        else
            home = new Directory(su, getUsername(), getManagerU().getDirHome(), getManagerU());
        home.setOwner(this);
        home.setPermissions(this.getPermissions());
        setHome(home);
    }

    /**
     * Initiate directory home at given location.
     */
    protected void initHome(String pHomePath) {
        User su = getManagerU().getSuperuser();
        Session s = new Session(su);
        Directory parent = (Directory) su.getFileByPath(pHomePath, s.getToken());
        Directory home;
        if(su == null)
            home = new Directory(this, getUsername(), parent, getManagerU());
        else
            home = new Directory(su, getUsername(), parent, getManagerU());
        home.setOwner(this);
        home.setPermissions(this.getPermissions());
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
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{3,}");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches())
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
        Manager m = Manager.getInstance();

        String name = node.getChild("name").getValue();
        String username = node.getAttribute("username").getValue();
        String password = node.getChild("password").getValue();
        short umask = Short.parseShort(node.getChild("mask").getValue());
        String path = node.getChild("home").getValue();
        String parentPath = path.substring(0,path.lastIndexOf("/"));
        parentPath = parentPath.equals("") ? "/" : parentPath;
        init(new String(name),new String(username),new String(password), new Short(umask), m);
        initHome(parentPath);
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
     * @param  link (String) receives a String with the link content.
     * @return  File returns the last File that appears in the path.
     */
    public File getFileByPath (String link, String token) throws FileNotFoundException, InvalidNameFileException {
    	if (link.length() > 1024){
    		throw new InvalidNameFileException(link);
    	}
    	if (link.equals("")){
    		throw new FileNotFoundException(link);
    	}
        if ( (link.charAt(link.length() - 1)== '/') && (link.length() > 1)){
        		link = link.substring(0, link.length() -1);
    	}
        if(link.equals(".")){
        	checkReadPermissions(getSessionDirectory(token));
        	return getValidSession().getCurrentDirectory();
        }
        else if(link.equals("..")){
        	checkReadPermissions(getSessionDirectory(token).getParent());
        	return getValidSession().getCurrentDirectory().getParent();
        }
        else if(link.startsWith("/")){
        	String[] split0 = link.split("/",2);
            String rest0 = split0[1];
            checkReadPermissions(Manager.getInstance().getHome().getFileByPath(rest0));
        	return Manager.getInstance().getHome().getFileByPath(rest0);
        }
        	checkReadPermissions(getSessionDirectory(token).getFileByPath(link));
        	return getSessionDirectory(token).getFileByPath(link);
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
     * Gets the first to be found valid session.
     * @return Session returns the valid session.
     */
    public Session getValidSession() {
        for (Session s: getSessionSet()) {
            if (!s.hasExpired())
                return s;
        }
        return null;
    }

    /**
     * Gets a session by a token.
     * @return Session returns the session.
     */
    public Session getSessionByToken(String token) {
        for (Session s: getSessionSet()) {
            if (s.getToken().equals(token))
                return s;
        }
        return null;
    }

    /**
     * Gets the session current directory.
     * @return Session returns session current directory.
     */
    public Directory getSessionDirectory(String token) {
        return getSessionByToken(token).getCurrentDirectory();
    }

    /**
     * Check if the password is correct.
     * @param password (String) represents the user password
     */
    public boolean isValidPassword(String password) {
        return getPassword() == password;
    }

    /**
     * Removes User and related objects.
     * @// FIXME: 18/03/16 doesn't work because needs to setHome(null); before the loop.
     */
    public void remove() {
        super.setManagerU(null);
        setHome(null);
        for(File i : getFileSet()) {
            i.remove(this);
        }
        setPermissions(null);
        deleteDomainObject();
    }
    
    /**
     * Check if User has a file.
     */
    public boolean hasFile(String nameFile) {
    	
    	if(this.getName().equals(nameFile))
    		return true;
    	for(File f : getFileSet()) {
            f.hasFile(nameFile);
        }
    	return false;
    }

    public void disconnectExpiredSessions()
    {
        for(Session i : getSessionSet())
        {
            if(i.hasExpired())
                i.remove();
        }
    }
    
    public void checkReadPermissions(File file) throws ReadPermissionException{
    	if (!getPermissions().canRead(file))
            throw new ReadPermissionException(file.getName(), this.getName());
    }
}
