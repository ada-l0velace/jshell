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
        Directory home = new Directory(this, username, new Link ("..", m.getDirHome(),"/home/", m),m);
        setHome(home);
        m.getDirHome().addFile(home);
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
     * Do override of setUsername checking for special characters
     * @param String (JavaPrimitive) which represents a username
     */
    @Override
    public void setUsername(String username) throws InvalidUsernameException {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches())
            throw new InvalidUsernameException(username);
        else
            super.setUsername(username);
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
        short umask = Short.parseShort(node.getAttribute("umask").getValue());
        Directory home;
        //Manager.log.trace(name);
        /*
        if (getHome() != null) {
            for (File f : getHome().getFileSet())
                f.remove();
        }
        */
        //if (getHome() == null) {
            //getHome().remove();

            setName(new String(name));
            setUsername(new String(username));
            setPassword(new String(password));
            setPermissions(new Permissions(umask));
            //Manager.log.trace(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
            //Manager.log.trace(username);
            Element dir = xml.getChild("Directory");
            home = new Directory(dir, this);
            setHome(home);
            //Manager.getInstance().getDirHome().addFile(home);
            //Manager.log.trace("<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        //}
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
        if (link.charAt(link.length() - 1)== '/'){
            link = link.substring(0, link.length() -1);
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
            if(spliTest[1].equals(getName())){
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
     * @param  Link link receives a link to a plain file. 
     * @return  String returns the string with the plain file content.
     */

    public String getPFileContentByLink(Link link){     
        PlainFile PF = (PlainFile)getFileByPath(link.getContent());
        return PF.getContent();
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
