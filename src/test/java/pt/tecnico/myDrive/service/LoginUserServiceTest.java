package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Set;
import java.util.HashSet;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.InvalidUserCredentialsException;

public class LoginUserServiceTest extends BaseServiceTest{




    private static final String _username = "shepard";
    private static final String _password = "commander";
    private String _token;
    private User _user;
    Set<EnvironmentVariable> eSet;
    
    protected void populate(){
        _user = createUser(_username, _password, "John",(short) 255);
        _token = createSession(_username, _password);

    }

    @Test
    public void success(){
        LoginUserService service = new LoginUserService(_username, _password);
        service.execute();
        //check session was created
        Session s = Manager.getInstance().getSessionByToken(service.result());
        
        assertNotNull("Session was not created", s);
        assertEquals("Invalid username", _username, s.getUser().getUsername());
    }

    @Test(expected = InvalidUserCredentialsException.class)
    public void wrongPassword(){
        LoginUserService service = new LoginUserService(_username, "general");
        service.execute();
    }
        
    @Test(expected = InvalidUserCredentialsException.class)
    public void wrongUsername(){
        LoginUserService service = new LoginUserService("miranda", _password);
        service.execute();
    }

    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername(){
        LoginUserService service = new LoginUserService("", _password);
        service.execute();
    }

    @Test(expected = InvalidUsernameException.class)
    public void lessThan3CharactersUsername(){
        LoginUserService service = new LoginUserService("a", _password);
        service.execute();
        }

    @Test
    public void invalidSessionsDeleted(){
        Manager m = Manager.getInstance();
        int i = 0;
        String [] tok = new String[7];
        Set<EnvironmentVariable> eSet = new HashSet<EnvironmentVariable>();
        while (i < 7){
            tok[i] = createSession(_username, _password);
            Session invalid = Manager.getInstance().getSessionByToken(tok[i]);
            for (EnvironmentVariable e : m.getSessionByToken(tok[i]).getEnvVarSet())
                eSet.add(e);
            invalid.setLastActive(invalid.getLastActive().minusHours(5));
            i++;    
        }

        LoginUserService service = new LoginUserService(_username, _password);
        service.execute();
        i = 0;
        User u = m.getUserByUsername(_username);
        while (i < 7){
            assertTrue("Environment variables not deleted", eSet.isEmpty() );
            assertNull("User still has invalid sessions", u.getSessionByToken(tok[i]));
            i++;
        }
        eSet = null;
    }
}
