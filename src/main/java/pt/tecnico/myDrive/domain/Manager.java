package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.UsernameAlreadyExistsException;
import pt.tecnico.myDrive.service.factory.Factory;
import pt.tecnico.myDrive.service.factory.FileFactoryProducer;
import pt.ist.fenixframework.FenixFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        new SuperUser(this);
        new Guest(this);
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
     * Creates a session for a specific username.
     * @param username (String) represents the username of the user.
     * @return token returns the token of the session created.
     */
    public String createSession(String username, String password) {
        Session s = new Session(username, password);
        return s.getToken();
    }

    /**
     * Get a user by username.
     * @param username (User) receives the username of the user we want to search for.
     * @see User
     * @return User returns the user if found, null otherwise.
     */
    public User getUserByUsername(String username) throws InvalidUsernameException {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{3,}");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches())
            throw new InvalidUsernameException(username);
        for(User u : getUsersSet()) {
            if(username.equals(u.getUsername())) {
                return u;
            }
        }
        return null;
    }

    public File createFile(Factory.FileType fileT, String token, String name, String content) {
        Factory factory = FileFactoryProducer.getFactory(fileT, token);
        return factory.CreateFile(name, content);
    }

    /**
     * Gets the SuperUser.
     * @return User which represents the superuser.
     */
    public User getSuperuser() {
        for(User u : getUsersSet()) {
            if (u.isSuperUser())
                return u;
        }
        return null;
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
            }
        }
        for (Element node : xml.getChild("Files").getChildren("Directory")) {
            new Directory(node);
        }
        for (Element node : xml.getChild("Files").getChildren("PlainFile")) {
            new PlainFile(node);
        }
        for (Element node : xml.getChild("Files").getChildren("Link")) {
            new Link(node);
        }
        for (Element node : xml.getChild("Files").getChildren("App")) {
            new App(node);
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
        user.remove();
        return true;
    }

    /**
     * Gets the session by a token.
     * @param token (String) receives the token string to find.
     * @return boolean returns the session if the token exists.
     */
    public Session getSessionByToken(String token) {
        Session s = null;
        for(User u : getUsersSet()) {
            s = u.getSessionByToken(token);
            if (s != null)
                return s;
        }
        return null;
    }

    /**
     * Gets the user by a token.
     * @param token (String) receives the token string to find.
     * @return boolean returns the user if the token exists.
     */
    public User getUserByToken(String token) {
        for(User u : getUsersSet())
            if(u.getSessionByToken(token) != null)
                return u;
        return null;
    }

    /**
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Document xmlExport(){
        Element node = new Element("Manager");
        Document doc = new Document(node);

        Element users = new Element("Users");
        node.addContent(users);
        Element files = new Element("Files");
        node.addContent(files);
        files.addContent(getHome().xmlExport());
        
        for (User u: getUsersSet()){
            users.addContent(u.xmlExport());
        }

        
        return doc;
    }

    
    /**
     * Main export xml.
     * @return Document represents the exported document.
     */
    public Document exportXml() {
        ArrayList<Element> elements = new ArrayList<Element>();
        Element node = new Element("Manager");
        Document doc = new Document(node);

        Element users = new Element("Users");
        node.addContent(users);
        Element files = new Element("Files");
        node.addContent(files);

        
        for (User u: getUsersSet()){
            if (!u.getUsername().equals("root"))
                users.addContent(u.exportXml());
        
            for (File f: u.getFileSet()){
                if (!(f.getName().equals("/") || (f.getPath().equals("/") && f.getName().equals("home")) || f.equals(u.getHome())))
                    elements.add(f.exportXml());   
            }
        }
        Collections.sort(elements, new Comparator<Element>(){
                public int compare(Element e1, Element e2){
                    return e1.getAttribute("id").getValue().compareTo(e2.getAttribute("id").getValue());
                }
            });
        for (Element e : elements)
            files.addContent(e);
        
        return doc;
    }

    public void deleteSessions()
    {
        super.getUsersSet().forEach((s) -> {
            s.removeExpiredSessions();
        });
    }

}
