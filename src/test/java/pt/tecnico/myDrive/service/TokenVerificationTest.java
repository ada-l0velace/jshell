package pt.tecnico.myDrive.service;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.UserNotInSessionException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;

public abstract class TokenVerificationTest extends BaseServiceTest {

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

    @Test
    public void sessionRenewed(){
        createUser("auron", "overdrive", "Auron", (short) 0xFF);

        String token = createSession("auron", "overdrive");
        Session s = Manager.getInstance().getSessionByToken(token);
        PlainFile plain = new PlainFile(s.getUser(), "Testdoc", "somecontent", s.getCurrentDirectory(), Manager.getInstance());
        
        s.setLastActive(s.getLastActive().minusHours(1));

        token = createSession("auron", "overdrive");
        s.setLastActive(s.getLastActive().minusHours(1));

        CreateService(token).execute();
        assertTrue(!Manager.getInstance().getSessionByToken(token).hasExpired());
        plain = null;
        }

}
