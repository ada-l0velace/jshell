package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import pt.tecnico.myDrive.exception.PublicAcessDeniedException;

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
     * @param user (User) receives a valid Login Instance.
     */
    public Session(User user) {
        this();
        setUser(user);
        setCurrentDirectory(user.getHome());
        this.setToken(user.getUsername()+ new BigInteger(64, new Random()).longValue());
    }

    /**
     * Method to check if a Session is Expired.
     * @return boolean returns true if is expired false if not.
     */
    public boolean hasExpired() {
        return !this.getLastActive().plusHours(2).isAfterNow();
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

    public void remove () {
        setCurrentDirectory(null);
        super.setUser(null);
        for(EnvironmentVariable i : getEnvVarSet())
            i.remove();
        deleteDomainObject();
    }

}
