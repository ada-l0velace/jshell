package pt.tecnico.myDrive.service;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.UserNotInSessionException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;

public abstract class TokenVerificationTest extends BaseServiceTest{

    public abstract MyDriveService CreateService(String token);
    
    
    @Test(expected = UserNotInSessionException.class)
    public void invalidTokenTest(){
        CreateService("wrong_token").execute();
    }

    @Test(expected = UserNotInSessionException.class)
        public void emptyTokenTest(){
        CreateService("").execute();
    }

    @Test(expected = UserSessionExpiredException.class)
    public void sessionExpired(){
        createUser("auron", "overdrive", "Auron", (short) 0xFF);
        String token = createSession("auron", "overdrive");
        Session s = Manager.getInstance().getSessionByToken(token);
        s.setLastActive(s.getLastActive().minusHours(5));
        CreateService(token).execute();
    }



}
