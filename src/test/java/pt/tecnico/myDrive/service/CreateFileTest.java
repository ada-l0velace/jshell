package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import static org.junit.Assert.assertFalse;

public class CreateFileTest extends TokenVerificationTest {
	
    private Directory _file;
    private static final String FILETYPEL = "L";
    private static final String FILETYPEP = "P";
    private static final String FILETYPED = "D";
    private static final String FILETYPEA = "A";
	private static final String FILENAME = "TestFile";
	private static final String USERNAME = "Jack";
	private static final String PASSWORD = "pwjack";
    private static final String CONTENT = "teste teste";
    private static final String NAME = "John";
    private static final Short UMASK = 0xF0;
    private static final Short UMASK0 = 0x00;
	private User _user;
	private String _token;
    private String _tokenNoAcess;
	
	
    protected void populate() {
		_user = createUser(USERNAME, PASSWORD, NAME, UMASK);
		_token = createSession(USERNAME);

        _user = createUser(USERNAME, PASSWORD, NAME, UMASK0);
        _tokenNoAcess = createSession(USERNAME);
    }

    @Test
    public void success() {
        CreateFile service = new CreateFile(_token, FILENAME, FILETYPEL);
        service.execute();

        // check file was created
        assertFalse("File was not created", !_user.hasFile(FILENAME));
    }
	
    @Test(expected = FileNotFoundException.class)
    public void deleteNonExistingFile() {
        CreateFile service = new CreateFile(_token, FILENAME, FILETYPEL);
        service.execute();
    }

    @Test
    public void createdPlainFile() {
        CreateFile service = new CreateFile(_token, FILENAME, FILETYPEP, CONTENT);
        service.execute();

        // check file was created
        assertFalse("File was not created", !_user.hasFile(FILENAME));
    }

    @Test(expected = ReadPermissionException.class)
    public void creatNonAcesivelFile() {
        CreateFile service = new  CreateFile(_tokenNoAcess, FILENAME, FILETYPEL);
        service.execute();
    }

    @Test(expected = ReadPermissionException.class)
    public void creatNonAcesivelPlainFile() {
        CreateFile service = new  CreateFile(_tokenNoAcess, FILENAME, FILETYPEP, CONTENT);
        service.execute();
    }

    
    @Override
    public MyDriveService CreateService(String token) {
        return new CreateFile(_token, FILENAME, FILETYPEL);
    }


}