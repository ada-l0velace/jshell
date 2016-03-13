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
     * Also initiates Super User root.
     */
    private Manager() {
        setLastFileId(0);
        SuperUser su = new SuperUser();
        RootDirectory rootDir = new RootDirectory(su,"/",this);
        Directory home = new Directory(su, "home", new Link ("..", rootDir, "/", this),this);
        su.setHome(home,this);
        home.addFile(su.getHome());
        setSuperuser(su);
        setHome(rootDir);
        rootDir.addFile(home);
        addUsers(su);
        FenixFramework.getDomainRoot().setManager(this);
        setRoot(FenixFramework.getDomainRoot());
    }

    /**
     * Implements getInstance from Singleton Design Pattern.
     * @return if no instance of Manager exists, returns a new instance of Manager, otherwise returns the existing Manager.
     */
    public static Manager getInstance() {
        Manager instance = FenixFramework.getDomainRoot().getManager();
        if(instance == null) {
            //Manager.log.trace("New Manager"); 
            return new Manager();
        }
        return instance;
    }

    public Directory getDirHome() {
        return (Directory) getHome().searchFile("home");
    }

    /**
     * Get a user by username.
     * @param String username : receives the username of the user we want to search for.
     * @return Returns the user if found, null otherwise.
     */
    public User getUserByUsername(String username) {
        for(User u : getUsersSet())
            if(username.equals(u.getUsername()))
                return u;
        return null;
    }

    /**
     * Checks if a certain user exists.
     * @param User user : receives the user to search for.
     * @return Returns true if the user was found, false otherwise.
     */
    public boolean existUser(User user) {
        if(getUserByUsername(user.getUsername()) != null)
            return true;
        else
            return false;
    }

    /**
     * [resourceFile description]
     * @param  filename (Java Primitive) name of the file
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
     * @param xml (JDOM library type) which represents a MyDrive element.
     */
    public void importXml(Element xml) {

        for (Element node : xml.getChild("Users").getChildren("User")) {
            String username = node.getAttribute("username").getValue();
            User user = getUserByUsername(username);
            //log.trace(username);
            //log.trace("-----------------");
            if (user == null) {
                user = new User(node);
                createUser(user);
            }
            else {
                //user.importXml(node);
            }
        }
    }

    /**
     *  Creates a new user.
     *  @param User user : receives the new user to create.
     */
    public void createUser(User user) throws UsernameAlreadyExistsException {
        if(existUser(user) != true) {
            addUsers(user);
            getDirHome().addFile(user.getHome());
        }
        else
            throw new UsernameAlreadyExistsException(user.getUsername());
    }

    /**
     * Creates a new user.
     * @param User user : receives the new user to delete.
     * @return Returns true if the user was deleted, false otherwise.
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
     * Searches for the Home Directory.
     * @return Directory returns directory /Home.
     */
    public Directory searchHome() {
        return (Directory) getHome().searchFile("Home");
    }
  
    public Document exportXml() {
        Element node = new Element("Manager");
        Document doc = new Document(node);

        node.addContent(getHome().exportXml());
        
        Element users = new Element("Users");
        node.addContent(users);

        for (User u: getUsersSet())
            users.addContent(u.exportXml());
        
        return doc;
    }

}
