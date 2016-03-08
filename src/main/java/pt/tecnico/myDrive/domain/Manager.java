package pt.tecnico.myDrive.domain;


public class Manager extends Manager_Base 
{ 

    private Manager() 
    {
        setRoot(FenixFramework.getDomainRoot());
        setSuperuser(new SuperUser()); 
        setHome(new Directory()); 
    }

    public static Manager getInstance() 
    {
        Manager instance = FenixFramework.getDomainRoot().getManager();

        if(instance == null)
        {
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

