package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.InvalidUserCredentialsException;

public class LoginUserTest extends BaseServiceTest{

    private static final String _username = "shepard";
    private static final String _password = "commander";
    private User _user;
    
    protected void populate(){
        _user = createUser(_username, _password, "John",(short) 255);
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
    public void invalidPassword(){
        LoginUser service = new LoginUser(_username, "general");
        service.execute();
    }
        
    @Test(expected = InvalidUserCredentialsException.class)
    public void invalidUsername(){
        LoginUser service = new LoginUser("miranda", _password);
        service.execute();
    }
    
}
