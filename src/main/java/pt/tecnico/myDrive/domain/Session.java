package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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
     * @// FIXME: 19/03/16 IMPLEMENT ME PLEASE
     */
    public void remove () {

    }



}
