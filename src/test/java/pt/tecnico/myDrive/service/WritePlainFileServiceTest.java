package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.WritePermissionException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.PublicAcessDeniedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;


public class WritePlainFileServiceTest extends TokenVerificationTest {
    

    private PlainFile _file;
    private PlainFile _file2;
    private static final String _fileName = "Testdoc";
    private static final String _testContent = "vanilla";
    private static final String _content = "ice";
    private static final String _password = "pwjackpwjack";
    private static final String _name = "Stevie";
    private static final Short _umask = 0xF8;
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
        _rootToken = createSession("root", "***");
        Session s = m.getSessionByToken(_rootToken);
        Session s2 = m.getSessionByToken(_token);
        new PlainFile(m.getSuperuser(), _fileName, _content , s.getCurrentDirectory(), m);
        new PlainFile(_user, _fileName + "1", _content , s2.getCurrentDirectory(), m);
        new Directory(_user, "DirToTheFuture", s2.getCurrentDirectory(), m);
        _file = new App(_user, "AppToThePast", _content , s2.getCurrentDirectory(), m);
        _pathLink = _user.createLink(_file, "LinkToThePast", s2.getCurrentDirectory(),m);
    }

    
    @Test
    public void success() {
        WritePlainFileService service = new WritePlainFileService(_token, _fileName+"1", _testContent);
        service.execute();
        PlainFile f = (PlainFile) _user.getFileByPath(_fileName+"1", _token);
        assertEquals("Content is not correct", f.getContent(_token), _testContent);
    }

    @Test(expected = FileNotFoundException.class)
    public void PlainFileNotFound() {
        WritePlainFileService service = new WritePlainFileService(_token, "boy", _testContent);
        service.execute();
    }

    @Test
    public void writeApp() {
        WritePlainFileService service = new WritePlainFileService(_token, "AppToThePast", _testContent);
        service.execute();
        PlainFile f = (PlainFile) _user.getFileByPath("AppToThePast", _token);
        assertEquals("Content is not correct", f.getContent(_token), _testContent);
    }

    @Test(expected = InvalidFileTypeException.class)
    public void writeDirectory() {
        WritePlainFileService service = new WritePlainFileService(_token, "DirToTheFuture",_testContent);
        service.execute();
    }

    @Test(expected = WritePermissionException.class)
    public void fileWriteAccessDenied() {
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_rootToken);
        Session s2 = m.getSessionByToken(_token);
        s2.setCurrentDirectory(s.getCurrentDirectory());

        WritePlainFileService service = new WritePlainFileService(_token, _fileName, _testContent);
        service.execute();
    }

    @Test(expected = PublicAcessDeniedException.class)
    public void setContentAcessDenied() {
        PlainFile plain = (PlainFile) _user.getFileByPath(_fileName + "1", _token);
        plain.setContent(_testContent);
    }

    /*
    @Test
    public void exportImportPlainFile() {
        PlainFile plain = (PlainFile) _user.getFileByPath(_fileName + "1", _token);
        Element el = plain.exportXml();
        WritePlainFileService service = new WritePlainFileService(_token, _fileName + "1", _testContent);
        service.execute();
        plain.importXml(el);
        assertEquals("Content is not correct", plain.getContent(_token), _content);
    }
    */

    @Override
    public MyDriveService CreateService(String token) {
        return new WritePlainFileService(token, _fileName, _testContent);
    }
    
}