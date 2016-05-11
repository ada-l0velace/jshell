
package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.service.factory.Factory.FileType;

import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Verifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class ReadPlainFileServiceTest extends TokenVerificationTest {

	private PlainFile _file;
	private static final String _fileName = "Testdoc";
    private static final String _fileName1 = "Filesemperm";
	private static final String _testContent = "vanilla";
	private static final String _password = "pwjackjack";
    private static final String _name = "Stevie";
    private static final Short _umask = 0xF0;
    private String _token;
    private User _user1;
    private String _token1;
    private File _pathLink;
    private String _token2;
	//-	private File _envLinkFile;
	
	
	protected void populate() {
		int i = 0;
        Manager m = Manager.getInstance();
		createUser("derp", _password, _name, _umask);
		_token = createSession("derp", _password);
        //Second user the read.
        _user1 = createUser("derp1", _password, _name, _umask);
        _token1 = createSession("derp1", _password);
        //User without permissions
        createUser("derp2", _password, _name, _umask);
        _token2 = createSession("derp2", _password);

        createFile(FileType.PLAINFILE, _token , _fileName, _testContent);
        createFile(FileType.PLAINFILE, _token , _fileName + "1", _testContent);
        createFile(FileType.DIRECTORY, _token , "DirToTheFuture");
        File app = createFile(FileType.PLAINFILE, _token , "AppToThePast", _testContent);

        _pathLink = createFile(FileType.LINK, _token , "LinkToThePast", app.getPath() + app.getName());

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
        File testPlainFile = createFile(FileType.PLAINFILE, _token2, _fileName1, _testContent);
        testPlainFile.getPermissions().setUmask((short)0x70);
        ReadPlainFileService service = new ReadPlainFileService(_token2, _fileName1);
        service.execute();
    }

    
    @Test(expected = ReadPermissionException.class)
    public void fileReadAccessDenied() {
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_token);
        Session s2 = m.getSessionByToken(_token1);
        // change user current directory.
        s2.setCurrentDirectory(s.getCurrentDirectory());


        ReadPlainFileService service = new ReadPlainFileService(_token1, _fileName + "1");
        service.execute();
    }
    
	/*	@Test
	public void environmentLinkSuccess(){
		new Expectations() {
			@Mocked("getContent")
			File _envLinkFile;
			{
				User u = Manager.getInstance().getUserByUsername("derp");
				u.getFileByPath(_envLinkFile.getContent(_token),_token).getContent(_token);
				result = _testContent;
			}
		};
		ReadPlainFileService service = new ReadPlainFileService(_token, _envLinkFile.getName());
		service.execute();
		assertEquals(service.result(), _testContent);
	}

	@Test(expected = ReadPermissionException.class)
	public void envLinkReadAccessDenied(){
		new Expectations(){
			{
				Manager.getInstance().getUserByToken(_token).getFileByPath(_envLinkFile.getContent(_token),_token).getContent(_token);
				result = new ReadPermissionException(_fileName,"derp");
			}
		};
		ReadPlainFileService service = new ReadPlainFileService(_token, "envLink");
		service.execute();		
		}*/
	
    @Override
    public MyDriveService CreateService(String token) {
        return new ReadPlainFileService(token, _fileName);
    }
    
}
