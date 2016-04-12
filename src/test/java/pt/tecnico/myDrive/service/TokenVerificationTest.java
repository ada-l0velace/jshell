package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.UserNotInSessionException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;

public abstract class TokenVerificationTest extends BaseServiceTest{

    public abstract MyDriveService testingService(String token);
    
    
    @Test(expected = UserNotInSessionException.class)
    public void invalidTokenTest(){
        testingService("wrong_token").execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void emptyTokenTest(){
        testingService("").execute();
    }

    @Test(expected = UserSessionExpiredException.class)
    public void sessionExpired(){
        createUser("auron", "overdrive", "Auron", (short) 0xFF);
        String token = createSession("auron");
        Session s = Manager.getInstance().getSessionByToken(token);
        s.setLastActive(s.getLastActive().minusHours(5));
        testingService(token).execute();
    }



}
