package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.InvalidUserCredentialsException;

public class LoginUserTest extends BaseServiceTest{




    private static final String _username = "shepard";
    private static final String _password = "commander";
    private String _token;
    private User _user;
    
    protected void populate(){
        _user = createUser(_username, _password, "John",(short) 255);
        _token = createSession(_username, _password);
    }

    @Test
    public void success(){
        LoginUser service = new LoginUser(_username, _password);
        service.execute();
        //check session was created
        Session s = Manager.getInstance().getSessionByToken(service.result());
        
        assertNotNull("Session was not created", s);
        assertEquals("Invalid username", _username, s.getUser().getUsername());
    }

    @Test(expected = InvalidUserCredentialsException.class)
    public void wrongPassword(){
        LoginUser service = new LoginUser(_username, "general");
        service.execute();
    }
        
    @Test(expected = InvalidUserCredentialsException.class)
    public void wrongUsername(){
        LoginUser service = new LoginUser("miranda", _password);
        service.execute();
    }

    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername(){
        LoginUser service = new LoginUser("", _password);
        service.execute();
    }

    @Test(expected = InvalidUsernameException.class)
    public void lessThan3CharactersUsername(){
        LoginUser service = new LoginUser("a", _password);
        service.execute();
        }

    @Test
    public void invalidSessionsDeleted(){
        int i = 0;
        String [] tok = new String[7];
        while (i < 7){
            tok[i] = createSession(_username, _password);
            Session invalid = Manager.getInstance().getSessionByToken(tok[i]);
            invalid.setLastActive(invalid.getLastActive().minusHours(5));
            i++;    
        }

        LoginUser service = new LoginUser(_username, _password);
        service.execute();
        Manager m = Manager.getInstance();
        i = 0;
        User u = m.getUserByUsername(_username);
        while (i < 7){
            assertNull("User still has invalid sessions", u.getSessionByToken(tok[i]));
            i++;
        }

    }

}
