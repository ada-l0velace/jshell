package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.UsernameAlreadyExistsException;

import pt.ist.fenixframework.FenixFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.jdom2.Element;
import org.jdom2.Document;


public class Manager extends Manager_Base{ 
    static final Logger log = LogManager.getRootLogger();
    
    protected Manager() {
        SuperUser su = new SuperUser();
        RootDirectory rootDir = new RootDirectory(su,"/",this);

        Directory home = new Directory(su, "home", new Link ("..", rootDir, "/"),this);
        su.setHome(home,this);
        home.addFile(su.getHome());
        setSuperuser(su);
        setHome(rootDir);
        rootDir.addFile(home);
        FenixFramework.getDomainRoot().setManager(this);
        setRoot(FenixFramework.getDomainRoot());
    }

    public static Manager getInstance() {
        Manager instance = FenixFramework.getDomainRoot().getManager();
        if(instance == null) {
            //Manager.log.trace("New Manager"); 
            return new Manager();
        }
        return instance;
    }

    public File getDirHome() {
        return getHome().searchFile("home");
    }

    public User getUser(User user) {
        for(User u : getUsersSet())
            if(u.getUsername().equals(user.getUsername()))
                return u;
        return null;
    }
    
    public User getUserByUsername(String username) {
        for(User u : getUsersSet())
            if(username.equals(u.getUsername()))
                return u;
        return null;
    }

    public boolean existUser(User user) {
        if(getUser(user) != null)
            return true;
        else
            return false;
    }

    public java.io.File resourceFile(String filename) {
        log.trace("Resource: "+filename);
        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader.getResource(filename) == null) return null;
        return new java.io.File(classLoader.getResource(filename).getFile());
    }

    public void importXml(Element xml) {
        for (Element node: xml.getChildren("users")) {
            String username = node.getAttribute("username").getValue();
            User user = getUserByUsername(username);

            if (user == null) // Does not exist
                user = new User(node);

            user.importXml(node);
        }
    }

    public void createUser(User user) throws UsernameAlreadyExistsException {
        if(existUser(user) != true)
            getUsersSet().add(user);
        else
            throw new UsernameAlreadyExistsException(user.getUsername());
    }

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
	public Directory searchHome(){
		return (Directory) getHome().searchFile("Home");
	}
  
  public Document exportXml(){
    Element node = new Element("Manager");
    Document doc = new Document(node);

    Element users = new Element("Users");
    node.addContent(users);

    
    for (User u: getUsersSet())
      users.addContent(u.exportXml());

    return doc;
  }

}
