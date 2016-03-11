package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.UsernameAlreadyExistsException;

import pt.ist.fenixframework.FenixFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.jdom2.Element;


public class Manager extends Manager_Base 
{ 
	static final Logger log = LogManager.getRootLogger();
	
    protected Manager() 
    {
        SuperUser su = new SuperUser();
        RootDirectory rd = new RootDirectory(su,"/");
        su.setHome(rd);
        setSuperuser(su);
        setRoot(FenixFramework.getDomainRoot());
        setHome(rd);
    }

    public static Manager getInstance() 
    {
        Manager instance = FenixFramework.getDomainRoot().getManager();
        if(instance == null) {
            Manager.log.trace("New Manager"); 
            return new Manager();
        }
        return instance;
    }

    public boolean existUser(User user)
    {
        if(getUser(user) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public User getUser(User user)
    {
        for(User u : getUsersSet())
        {
            if(u.getUsername().equals(user.getUsername()))
            {
                return u;
            }
        }
        return null;
    }

    public User getUserByUsername(String username) {
        for(User u : getUsersSet())
            if(username.equals(u.getUsername()))
                return u;
        return null;
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

    public void createUser(User user) throws UsernameAlreadyExistsException
    {
        if(existUser(user) != true)
        {
            getUsersSet().add(user);
        }
        else
        {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }

    public boolean deleteUser(User user)
    {
        if(existUser(user))
        {
            for(User u : getUsersSet())
            {
                if(u.getUsername().equals(user.getUsername()))
                {
                    u.remove();
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }
}

