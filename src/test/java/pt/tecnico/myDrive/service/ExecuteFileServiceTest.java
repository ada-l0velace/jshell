package pt.tecnico.myDrive.service;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Session;

import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.ExecutePermissionException;

import pt.tecnico.myDrive.service.factory.Factory;
import pt.tecnico.myDrive.service.factory.FileFactoryProducer;

public class ExecuteFileServiceTest extends TokenVerificationTest 
{
    private Directory _directory1;
    private Directory _directory2;
    private Link _link1;
    private Link _link2;
    private App _app1;
    private App _app2;
    private static final String _directoryName1 = "Documents1";
    private static final String _directoryName2 = "Documents2";
    private static final String _linkName1 = "Link1";
    private static final String _linkName1 = "Link2";
    private static final String _appName1 = "Application1";
    private static final String _appName2 = "Application2";
    private static final String _appContent1 = "pt.tecnico.myDrive.Main";
    private static final String _appContent2 = "pt.tecnico.myDrive.Main";
    private static final String _name1 = "Name1";
    private static final String _name2 = "Name2";
    private static final String _username1 = "User1";    
    private static final String _username2 = "User2";
    private static final String _password1 = "password1";
    private static final String _password2 = "password2";
    private static final Short _umask1 = 0xFB;
    private static final Short _umask2 = 0xBF;
    private User _user1;
    private User _user2;
    private String _token1;
    private String _token2;
    private Session _session1;
    private Session _session2;

    protected void populate() 
    {
        _user1 = createUser(_username1, _password1, _name1, _umask1);
        _user2 = createUser(_username2, _password2, _name2, _umask2);
        _token1 = createSession(_username1, _password1);
        _token2 = createSession(_username2, _password2);
        _session1 = Manager.getInstance().getSessionByToken();
        _session2 = Manager.getInstance().getSessionByToken();
        _directory1 = new Directory(_user1, _directoryName1, _session1.getCurrentDirectory(), Manager.getInstance());
        _directory2 = new Directory(_user2, _directoryName2, _session1.getCurrentDirectory(), Manager.getInstance());
        _app1 = new App(_user1, _appName1, _appContent1, session1.getCurrentDirectory(), Manager.getInstance());
        _app2 = new App(_user2, _appName2, _appContent2, session2.getCurrentDirectory(), Manager.getInstance());
        _link1 = _user1.createLink(_app1, _linkName1, session1.getCurrentDirectory(), Manager.getInstance());
        _link2 = _user2.createLink(_app2, _linkName2, session2.getCurrentDirectory(), Manager.getInstance());
    }

    @Test
    public void success()
    {
        
    }

    // Test - User1 run App1 - Expectation 1
    // Test - User1 run App2 - ExecutePermissionException 
    // Test - User2 run App1 - Expectation 1
    // Test - User2 run App2 - ExecutePermissionException
    // Test - User1 run Link1 - Expectation 1
    // Test - User1 run Link2- ExecutePermissionException
    // Test - User2 run Link1 - Expectation 1
    // Test - User2 run Link2 - ExecutePermissionException
    // Test - User1 run Directory1 - Exception
    // Test - User1 run PlainFile -  Exception
    // Test - User1 run Non existing file - Exception
    // Test - User1 run Chain of Links - Expectation 
    
}
