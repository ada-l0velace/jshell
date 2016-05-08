package pt.tecnico.myDrive.domain;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Element;

import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;

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
        //Session s = new Session(su);
        Directory home;

        if(su == null)
            home = new Directory(this, getUsername(), (Directory) getManagerU().getHome().searchFile("home"), getManagerU());
        else
            home = new Directory(su, getUsername(), (Directory) getManagerU().getHome().searchFile("home"), getManagerU());
        home.setOwner(this);
        home.setPermissions(this.getPermissions());
        setHome(home);
    }

    /**
     * Initiate directory home at given location.
     */
    protected void initHome(String pHomePath) {
        User su = getManagerU().getSuperuser();
        //Session s = new Session(su);
        Directory parent = (Directory) getManagerU().getHome().searchFile(pHomePath);
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
        node.setAttribute("password", super.getPassword());
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
        password.addContent(super.getPassword());
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
        	return getSessionByToken(token).getCurrentDirectory();
        }
        else if(link.equals("..")){
        	checkReadPermissions(getSessionDirectory(token).getParent());
        	return getSessionByToken(token).getCurrentDirectory().getParent();
        }
        else if(link.startsWith("/")){
        	String[] split0 = link.split("/",2);
            String rest0 = split0[1];
            checkReadPermissions(Manager.getInstance().getHome().getFileByPath(rest0));
        	//Manager.getInstance().log.trace(rest0);
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
	 * returns true if user has valid sessions
	 * @return boolean indicating if there are active sessions
	 */
	public boolean hasValidSessions(){		
		for (Session s : super.getSessionSet()){
			if (!s.hasExpired())
				return true;
		}
		return false;
	}

	/**
	 * returns true if user has logged in
	 * @return boolean indicating if the user has logged in
	 */
	public boolean hasLoggedIn(){
		return !super.getSessionSet().isEmpty();
	}
	
    /**
     * Gets a session by a token.
     * @return Session returns the session.
     */
    public Session getSessionByToken(String token) {
        for (Session s: super.getSessionSet()) {
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
        return super.getPassword().equals(password);
    }

    @Override
    public void addSession(Session s) {
        if (!super.getSessionSet().contains(s)){
            super.addSession(s);
            return;
        }
        throw new TokenIsNotUniqueException();
    }

    /**
     * Removes User and related objects.
     */
    public void remove() {
        super.setManagerU(null);
        setHome(null);
        super.getFileSet().forEach((f) -> f.remove());
        setPermissions(null);
        deleteDomainObject();
    }

    /**
     * Protection to protect listing the sessions to public.
     * @throws PublicAcessDeniedException
     */
    public Set<Session> getSessionSet() {
        throw new PublicAcessDeniedException("getSessionSet()", "getSessionDirectory(String token)");
    }

    /**
     * Removes the expired sessions from the user.
     */
    public void removeExpiredSessions() {
        super.getSessionSet().forEach((s) -> {
            if(s.hasExpired())
                s.remove();
        });
    }

    /**
     * Function to set the token live time of the user
     * @param date
     * @return boolean returns true if it expired false otherwise.
     */
    public boolean hasExpired(DateTime date) {
        return !date.plusMinutes(120).isAfterNow();
    }


    /**
     * Checks if the file has permissions to be read.
     * @param file receives the file to check
     * @throws ReadPermissionException occurs when the user doesn't have permissions to read.
     */
    public void checkReadPermissions(File file) throws ReadPermissionException {
    	if (!getPermissions().canRead(file))
            throw new ReadPermissionException(file.getName(), this.getName());
    }

    /**
     * Protection for the password not be viewed by others
     * @return Nothing
     */
    @Override
    public String getPassword() {
        throw new PublicAcessDeniedException("GetPassword", "IsValidPassword");
    }

    protected void setPasswordAux(String password) {
        super.setPassword(password);
    }

    /**
     * Sets a password of the User.
     * @param password
     * @throws InvalidPasswordException
     */
    public void setPassword(String password) throws InvalidPasswordException {
        if (password.length() < 8)
           throw new InvalidPasswordException();
        super.setPassword(password);
    }

    /**
     * Override of operator equal to a specific one.
     * @param o represents the other object.
     * @return boolean returns 1 if the name is equal to the other.
     */
    public int compareTo(User o) {
        return o.getUsername().compareTo(getUsername());
    }
}
