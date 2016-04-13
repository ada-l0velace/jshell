package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.DeletePermissionException;


public class DeleteFileTest extends TokenVerificationTest {
	
    private Directory _file;
	private static final String _fileName = "Documents";
	private static final String _fileType = "Directory";
	private static final String _username = "Jonny";
	private static final String _password = "jopass";
    private static final String _name = "John";
    private static final Short _umask = 0xF0;
	private User _user;
	private String _token;
	
	
    protected void populate() {
		_user = createUser(_username, _password, _name, _umask);
		_token = createSession(_username);
		_file = new Directory(_user, _fileName, Manager.getInstance().getSessionByToken(_token).getCurrentDirectory(),
							Manager.getInstance());   
    }

    @Test
    public void success() {
        DeleteFile service = new DeleteFile(_token, _fileName);
        service.execute();

        // check file was deleted
        assertFalse("File was not deleted", _user.hasFile(_fileName));
    }
	
    @Test(expected = FileNotFoundException.class)
    public void deleteNonExistingFile() {
        DeleteFile service = new DeleteFile(_token, "Images");
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteDotDotPermissionDenied() {
    	DeleteFile service = new DeleteFile(_token, "..");
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteDotPermissionDenied() {
    	DeleteFile service = new DeleteFile(_token, ".");
        service.execute();
    }
    
    @Test(expected = DeleteRootDirectoryException.class)
    public void deleteRootDirPermissionDenied() {
    	DeleteFile service = new DeleteFile(_token, "/");
        service.execute();
    }
    
    @Override
    public MyDriveService CreateService(String token) {
        return new DeleteFile(token, _fileName);
    }
}