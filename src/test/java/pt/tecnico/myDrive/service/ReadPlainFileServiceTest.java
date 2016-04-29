package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class ReadPlainFileServiceTest extends TokenVerificationTest {

	private PlainFile _file;
	private static final String _fileName = "Testdoc";
    private static final String _fileName1 = "Filesemperm";
	private static final String _testContent = "vanilla";
	private static final String _password = "pwjack";
    private static final String _name = "Stevie";
    private static final Short _umask = 0xF0;
    private static final Short _umask0 = 0x70;

    private User _user;
	private String _token;
    private Link _pathLink;
    private PlainFile _testPlainFile;

    private User _user1;
    private String _token1;

    private User _user2;
    private String _token2;

    private PlainFile _file2;

    private String _rootToken;
	private String _nonPremitionsToken;

	
	protected void populate() {
		int i = 0;
        Manager m = Manager.getInstance();
		_user = createUser("derp", _password, _name, _umask);
		_token = createSession("derp", _password);
        //Second user the read.
        _user1 = createUser("derp1", _password, _name, _umask);
        _token1 = createSession("derp1", _password);
        //User without premissions
        _user2 = createUser("derp2", _password, _name, _umask);
        _token2 = createSession("derp2", _password);
        //Root token.
        _rootToken = createSession("root", "***");
        //Change directory to check read permissions
        Session s = m.getSessionByToken(_token);
        new PlainFile(m.getSuperuser(), _fileName, _testContent , s.getCurrentDirectory(), m);
        new PlainFile(_user, _fileName + "1", _testContent , s.getCurrentDirectory(), m);
        new Directory(_user, "DirToTheFuture", s.getCurrentDirectory(), m);
        _file = new App(m.getSuperuser(), "AppToThePast", _testContent , s.getCurrentDirectory(), m);
        _pathLink = _user.createLink(_file, "LinkToThePast", s.getCurrentDirectory(),m);

    }

    @Test
    public void success() {
        ReadPlainFileService service = new ReadPlainFileService(_token, _fileName);
        service.execute();
        assertEquals("Content is not returned", service.result(), _testContent);
    }

    @Test(expected = FileNotFoundException.class)
    public void PlainFileNotFound() {
        ReadPlainFileService service = new ReadPlainFileService(_token, "Bin");
        service.execute();

    }

    @Test
    public void readApp() {
        ReadPlainFileService service = new ReadPlainFileService(_token, "AppToThePast");
        service.execute();
        assertEquals("Content is not returned", service.result(), _testContent);
    }

    @Test
    public void readLink() {
        ReadPlainFileService service = new ReadPlainFileService(_token, "LinkToThePast");
        service.execute();
        assertEquals("Content is not returned", service.result(), _pathLink.getContent(_token));
    }

    @Test(expected = InvalidFileTypeException.class)
    public void readDirectory() {
        ReadPlainFileService service = new ReadPlainFileService(_token, "DirToTheFuture");
        service.execute();
    }

    @Test
    public void readPublicPlainFile() {
        ReadPlainFileService service = new ReadPlainFileService(_token, _fileName);
        service.execute();
        assertEquals("Content is not returned", service.result(), _testContent);
    }

    @Test(expected = ReadPermissionException.class)
    public void fileReadUserAccessDenied() {
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_token2);
        _testPlainFile = new PlainFile(_user2, _fileName1, _testContent,s.getCurrentDirectory(), m);
        _testPlainFile.getPermissions().setUmask((short)0x70);


        ReadPlainFileService service = new ReadPlainFileService(_token2, _fileName1);
        service.execute();
    }

    /*
    @Test(expected = ReadPermissionException.class)
    public void fileReadAccessDenied() {
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_token2);
        _testPlainFile = new PlainFile(_user2, _fileName1, _testContent,s.getCurrentDirectory(), m);
        _testPlainFile.getPermissions().setUmask((short)0x70);


        ReadPlainFileService service = new ReadPlainFileService(_token2, _fileName1);
        service.execute();
    }
    */

    @Override
    public MyDriveService CreateService(String token) {
        return new ReadPlainFileService(token, _fileName);
    }
    
}
