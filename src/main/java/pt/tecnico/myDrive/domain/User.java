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
        setManagerU(m);
        init(name, username, password, umask, m);
        m.createUser(this);
    }
    
    /**
     * Protected constructor to init a user.
     * @param name     (String) represents the real name.
     * @param username (String) represents the username.
     * @param password (String) represents the password.
     * @param umask    (Short)  represents the permissions umask.
     */
    protected void init(String name, String username, String password, Short umask, Manager m) {
        setManagerU(m);
        setName(name);
        setUsername(username);
        setPassword(password);
        setPermissions(new Permissions(umask));
        // Manager.log.trace(Manager.getInstance().getHome());
        Directory home = new Directory(this, username, m.getDirHome(), m);
        setHome(home);
        //m.getDirHome().addFile(home);
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
     * @return Link returns the Link created.
     */
    public Link createLink(File file, String name) {
        Manager m = getManagerU();
        File f = file.getParent();
        Directory p = file.getParent();
        String l = "";
        while (f != null) {
            if (f.getParent() != null)
                l = f.getName()+ "/" + l;
            else
                l = f.getName() + l;
            f = f.getParent();
        }
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
     * Gets the file by a path.
     * @// FIXME: 18/03/16 needs refactoring.
     * @param  link (String) receives a String with the link content.
     * @return  File returns the last File that appears in the path.
     */
    public File getFileByPath (String link) throws FileNotFoundException {
        if (link.charAt(link.length() - 1)== '/'){
        	if (link.length() > 1){
        		link = link.substring(0, link.length() -1);
        	}
    	}
    	if(link.charAt(0) == '/'){
    		String[] spliTest = link.split("/");
        	if (spliTest.length == 0){
        		return Manager.getInstance().getHome();
        	}
    		String[] noBar = link.split("/",2);
    		return Manager.getInstance().getHome().getFileByPath(noBar[1]);
    	}
    	String[] spliTest = link.split("/");
    	if (spliTest.length == 1){
    		if(spliTest[0].equals(getUsername())){
    		    return getHome();
    		}
    		else{
    			return Manager.getInstance().getDirHome();
    		}
    	}
    	String[] split = link.split("/",2);
    	String rest = split[1];
    	String nomeInit = split[0];
    	if(nomeInit.equals("home")){
    		return Manager.getInstance().getDirHome().getFileByPath(rest);
    	}
    	else if (nomeInit.equals(getUsername())){
    		return getHome().getFileByPath(rest);
    	}
    	throw new FileNotFoundException(nomeInit);
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
        setManagerU(null);
        for(File i : getFileSet()) {
            i.remove();
        }
        setHome(null);
        setPermissions(null);
        deleteDomainObject();
    }
}
