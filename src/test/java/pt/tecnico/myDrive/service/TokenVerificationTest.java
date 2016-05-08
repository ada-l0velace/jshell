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

    private static final String NAME = "Auron";
    private static final String USERNAME = "auron";
    private static final String PASSWORD = "overdrive";

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
        createUser(USERNAME, PASSWORD, NAME, (short) 0xFF);
        String token = createSession(USERNAME, PASSWORD);
        Session s = Manager.getInstance().getSessionByToken(token);
        s.setLastActive(s.getLastActive().minusHours(5));
        CreateService(token).execute();
    }

	@Test(expected = UserSessionExpiredException.class)
	public void rootSessionExpired(){
		User sudo = Manager.getInstance().getSuperuser();
		String sudoToken = createSession(sudo.getUsername(), "***");
		Session s = Manager.getInstance().getSessionByToken(sudoToken);
		s.setLastActive(s.getLastActive().minusMinutes(11));
		CreateService(sudoToken).execute();
	}
	
    @Test
    public void sessionRenewed(){
        createUser(USERNAME, PASSWORD, NAME, (short) 0xFF);

        String token = createSession(USERNAME, PASSWORD);
        Session s = Manager.getInstance().getSessionByToken(token);
        new PlainFile(s.getUser(), "Testdoc", "somecontent", s.getCurrentDirectory(), Manager.getInstance());
        
        s.setLastActive(s.getLastActive().minusHours(1));

        token = createSession(USERNAME, PASSWORD);
        s.setLastActive(s.getLastActive().minusHours(1));

        CreateService(token).execute();
        assertTrue(!Manager.getInstance().getSessionByToken(token).hasExpired());
        }

}
