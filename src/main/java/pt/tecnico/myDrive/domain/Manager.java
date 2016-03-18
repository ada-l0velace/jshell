package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.UsernameAlreadyExistsException;

import pt.ist.fenixframework.FenixFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.jdom2.Element;
import org.jdom2.Document;


/**
 * This class implements a singleton design pattern.
 */
public class Manager extends Manager_Base{ 
    static final Logger log = LogManager.getRootLogger();
    
    /**
     * Creates a Manager, the singleton object of the application. 
     * Also initiates a SuperUser, RootDirectory and default system Directories.
     * @see SuperUser RootDirectory Directory
     */
    private Manager() {
        setLastFileId(0);
        SuperUser su = new SuperUser();
        RootDirectory rootDir = new RootDirectory(su,"/",this);
        Directory home = new Directory(su, "home", new Link ("..", rootDir, "/", this),this);
        Directory usr = new Directory(su, "usr", new Link ("..", rootDir, "/", this),this);
        Directory local = new Directory(su, "local", new Link ("..", usr, "/usr/", this),this);
        su.setHome(home,this);
        home.addFile(su.getHome());
        setSuperuser(su);
        setHome(rootDir);
        rootDir.addFile(home);
        rootDir.addFile(usr);
        usr.addFile(local);
        addUsers(su);
        FenixFramework.getDomainRoot().setManager(this);
        setRoot(FenixFramework.getDomainRoot());
    }

    /**
     * Gets the Manager Singleton Instance.
     * @return Manager returns the Manager single instance.
     */
    public static Manager getInstance() {
        Manager instance = FenixFramework.getDomainRoot().getManager();
        if(instance == null) {
            //Manager.log.trace("New Manager");
            return new Manager();
        }
        return instance;
    }

    /**
     * Gets the /home instance Directory
     * @see Directory
     * @return Directory represents the Directory Instance.
     */
    public Directory getDirHome() {
        return (Directory) getHome().searchFile("home");
    }

    /**
     * Get a user by username.
     * @param username (User) receives the username of the user we want to search for.
     * @see User
     * @return User returns the user if found, null otherwise.
     */
    public User getUserByUsername(String username) {
        for(User u : getUsersSet())
            if(username.equals(u.getUsername()))
                return u;
        return null;
    }

    /**
     * Checks if a certain user exists.
     * @param user (User) receives the user to search for.
     * @see User
     * @return Boolean returns true if the user was found, false otherwise.
     */
    public boolean existUser(User user) {
        if(getUserByUsername(user.getUsername()) != null)
            return true;
        else
            return false;
    }

    /**
     * Gets a resource file.
     * @param  filename (Java Primitive) name of the file
     * @see java.io.File
     * @return java.io.file which represents the file instance.
     */
    public java.io.File resourceFile(String filename) {
        log.trace("Resource: "+filename);
        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader.getResource(filename) == null) return null;
        return new java.io.File(classLoader.getResource(filename).getFile());
    }

    /**
     * Main import for Manager.
     * @see User
     * @param xml (JDOM library type) which represents a MyDrive element.
     */
    public void importXml(Element xml) {

        for (Element node : xml.getChild("Users").getChildren("User")) {
            String username = node.getAttribute("username").getValue();
            User user = getUserByUsername(username);
            
            if (user == null) {
                user = new User(node);
                addUsers(user);
                getDirHome().addFile(user.getHome());
            }
            else {
                user.importXml(node);
                getDirHome().addFile(user.getHome());   
            }
        }
    }

    /**
     *  Creates a new user.
     *  @see User
     *  @param user (User) receives the new user to create.
     */
    public void createUser(User user) throws UsernameAlreadyExistsException  {
        if(getUserByUsername(user.getUsername()) == null) {
            addUsers(user);
        }
        else {

            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }

    /**
     * Creates a new user.
     * @param user (User) receives the new user to delete.
     * @return Boolean returns true if the user was deleted, false otherwise.
     */
    public boolean deleteUser(User user) {
        if(existUser(user))
            for(User u : getUsersSet()) {
                if(u.getUsername().equals(user.getUsername())){
                    u.remove();
                    return true;
                }
            }
        return false;
    }

    /**
     * Main export xml.
     * @return Document represents the exported document.
     */
    public Document exportXml() {
        Element node = new Element("Manager");
        Document doc = new Document(node);
        //node.addContent(getHome().exportXml()); 
        Element users = new Element("Users");
        node.addContent(users);

        for (User u: getUsersSet())
            users.addContent(u.exportXml());
        
        return doc;
    }

}
