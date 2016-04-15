package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.WritePermissionException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;


public class WritePlainFileTest extends TokenVerificationTest {
    

    private PlainFile _file;
    private PlainFile _file2;
    private static final String _fileName = "Testdoc";
    private static final String _testContent = "vanilla";
    private static final String _content = "ice";
    private static final String _password = "pwjack";
    private static final String _name = "Stevie";
    private static final Short _umask = 0xF0;
    private static final Short _umask1 = 0x00;

    private User _user;
    private String _token;

    private User _user1;
    private String _token1;

    private Link _pathLink;

    private String _rootToken;
    private String _nonPremitionsToken;
    
    protected void populate() {
        int i = 0;
        Manager m = Manager.getInstance();
        _user = createUser("derp", _password, _name, _umask);
        _token = createSession("derp", _password);
        _user1 = createUser("derp1", _password, _name, _umask1);
        _token1 = createSession("derp1", _password);
        _rootToken = createSession("root", "***");
        Session s = m.getSessionByToken(_token);
        new PlainFile(m.getSuperuser(), _fileName, _testContent , s.getCurrentDirectory(), m);
        new PlainFile(_user, _fileName + "1", _testContent , s.getCurrentDirectory(), m);
        new Directory(_user, "DirToTheFuture", s.getCurrentDirectory(), m);
        _file = new App(m.getSuperuser(), "AppToThePast", _testContent , s.getCurrentDirectory(), m);
        _pathLink = _user.createLink(_file, "LinkToThePast", s.getCurrentDirectory(),m);;
    }

    
    @Test
    public void success() {
        WritePlainFile service = new WritePlainFile(_token, _fileName, _content);
        service.execute();
        assertNotEquals("Content is not correct", service.result(), _content);
    }

    @Test(expected = FileNotFoundException.class)
    public void PlainFileNotFound() {
        WritePlainFile service = new WritePlainFile(_token, "boy", _content);
        service.execute();
    }

    @Test
    public void writeApp() {
        WritePlainFile service = new WritePlainFile(_token, "AppToThePast",_content);
        service.execute();
        assertNotEquals("Content is not correct", service.result(), _content);
    }

    @Test(expected = InvalidFileTypeException.class)
    public void writeDirectory() {
        WritePlainFile service = new WritePlainFile(_token, "DirToTheFuture",_content);
        service.execute();
    }

    @Test(expected = WritePermissionException.class)
    public void fileWriteAccessDenied() {
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_token);
        Session s2 = m.getSessionByToken(_token1);
        s2.setCurrentDirectory(s.getCurrentDirectory());

        WritePlainFile service = new WritePlainFile(_token1, _fileName, _content);
        service.execute();
    }

    @Override
    public MyDriveService CreateService(String token) {
        return new WritePlainFile(token, _fileName, _content);
    }
    
}