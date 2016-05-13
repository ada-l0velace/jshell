package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import pt.tecnico.myDrive.exception.InvalidUserCredentialsException;
import pt.tecnico.myDrive.exception.PublicAcessDeniedException;
import pt.tecnico.myDrive.exception.TokenIsNotUniqueException;

import java.math.BigInteger;
import java.util.Random;

public class Session extends Session_Base {

    /**
     * Default constructor.
     */
    public Session() {
        super();
        this.setLastActive(new DateTime(DateTimeZone.UTC));
    }

    /**
     * Alternate constructor to create new session.
     * @param username (String) receives a username string.
     * @param password (String) receives a password string.
     */
    public Session(String username, String password) {
        this();
        User user = Manager.getInstance().getUserByUsername(username);
        if (user == null)
            throw new InvalidUserCredentialsException();
        if(!user.isValidPassword(password))
            throw new InvalidUserCredentialsException();
        while (true) {
            try {
                super.setToken(user.getUsername() + new BigInteger(64, new Random()).longValue());
                setUser(user);
                break;
            } catch(TokenIsNotUniqueException e) {
                continue;
            }
        }
        setCurrentDirectory(user.getHome());

    }

    public EnvironmentVariable environmentVarExists(String name) {
        for (EnvironmentVariable ev : getEnvVarSet()) {
            if (ev.equals(name)) {
                return ev;
            }
        }
        return null;
    }

    /**
     * Method to check if a Session is Expired.
     * @return boolean returns true if is expired false if not.
     */
    public boolean hasExpired() {
        boolean a = getUser().hasExpired(getLastActive());
        return a;
    }

    /**
     * Sets the session the to user session list.
     * @param u user to apply the session.
     */
    @Override
    public void setUser(User u) {
        if (u == null) {
            remove();
        }
        else {
            u.addSession(this);
            //super.setUser(u);
        }
    }

    public void modifyEnvVar(String name, String value) {
        EnvironmentVariable ev = environmentVarExists(name);
        if (ev != null) {
            ev.modify(name, value);
        }
    }

    /**
     * Protection changing tokens to public.
     * @throws PublicAcessDeniedException
     */
    @Override
    public void setToken(String token) {
        throw new PublicAcessDeniedException("setToken(token)","super.setToken(token)");
    }

    /**
     * Removes the Session from the domain.
     */
    public void remove () {
        setCurrentDirectory(null);
        super.setUser(null);
        for(EnvironmentVariable i : getEnvVarSet())
            i.remove();
        deleteDomainObject();
    }



    /**
     * Override of operator equal to a specific one.
     * @param o represents the other object.
     * @return boolean returns 1 if the name is equal to the other.
     */
    public int compareTo(Session o) {
        return o.getToken().compareTo(getToken());
    }

}
