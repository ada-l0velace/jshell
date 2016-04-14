package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.WritePermissionException;


public class WritePlainFileTest extends TokenVerificationTest {
    
    private Directory _directoryName;
    private PlainFile _file;
    private User _user;
    private User _userNoWrite;
    private String _token;
    private String _tokenNoWrite;
    private static final String _fileName = "Documents";
    private static final String _plainFileName = "Biana";
    private static final String _username = "Yommere";
    private static final String _usernameNoWrite = "Lymmuar";
    private static final String _password = "badjoras";
    private static final String _name = "Daniel";
    private static final Short  _umask = 0xF0;    
    private static final Short  _umaskNoWrite = 0x40;    
    private String _content = "Testing 1,2,3 !";
    
    protected void populate() {
        _user = createUser(_username, _password, _name, _umask);
        _userNoWrite = createUser(_usernameNoWrite, _password, _name, _umaskNoWrite);
        _token = createSession(_username, _password);
        _tokenNoWrite = createSession(_usernameNoWrite, _password);
    }

    
    @Override
    public MyDriveService CreateService(String token) {
        return new WritePlainFile(token, _fileName, _content);
    }
}