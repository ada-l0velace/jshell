package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.DeletePermissionException;
import pt.tecnico.myDrive.exception.DeleteRootDirectoryException;
import pt.tecnico.myDrive.exception.SpecialDirectoriesException;


public class DeleteFileTest extends TokenVerificationTest {
	
    private Directory _dir;
    private App _app;
    private Link _link;
	private static final String _dirName = "Documents";
	private static final String _appName = "App";
	private static final String _linkName = "Link";
    private static final String _name = "John";
    private static final String _name2 = "Mike";
    private static final String _username = "Jonny";	
    private static final String _username2 = "Mikky";
	private static final String _password = "jopass";
	private static final String _password2 = "mipass";
    private static final Short _umask = 0xF8;
	private User _user;
	private User _user2;
	private String _token;
	private String _token2;
	private String _tokenroot;	
	
    protected void populate() {
		_user = createUser(_username, _password, _name, _umask);
		_user2 = createUser(_username2, _password2, _name2, _umask);
		_token = createSession(_username);
		_token2 = createSession(_username2);
		_tokenroot = createSession("root");
		_dir = new Directory(_user, _dirName, Manager.getInstance().getSessionByToken(_token).getCurrentDirectory(),
							Manager.getInstance());
		_app = new App(_user, _appName, " ", Manager.getInstance().getSessionByToken(_token).getCurrentDirectory(),
				Manager.getInstance());
		_link = _user.createLink(_dir, _linkName,
				Manager.getInstance().getSessionByToken(_token).getCurrentDirectory(), Manager.getInstance());
    }

    @Test
    public void successDir() {
        DeleteFile service = new DeleteFile(_token, _dirName);
        service.execute();
        Session session = Manager.getInstance().getSessionByToken(_token);
        // check file was deleted
        assertNull("Directory was not deleted", session.getCurrentDirectory().search(_dirName, _token));
    }
    
    @Test
    public void successApp() {
        DeleteFile service = new DeleteFile(_token, _appName);
        service.execute();
        Session session = Manager.getInstance().getSessionByToken(_token);
        // check file was deleted
        assertNull("App was not deleted", session.getCurrentDirectory().search(_appName, _token));
    }
	
    @Test
    public void successLink() {
        DeleteFile service = new DeleteFile(_token, _linkName);
        service.execute();
        Session session = Manager.getInstance().getSessionByToken(_token);
        // check file was deleted
        assertNull("Link was not deleted", session.getCurrentDirectory().search(_linkName, _token));
    }
    
    @Test(expected = FileNotFoundException.class)
    public void deleteNonExistingFile() {
        DeleteFile service = new DeleteFile(_token, "Images");
        service.execute();
    }
    
    @Test(expected = SpecialDirectoriesException.class)
    public void deleteDotDotPermissionDenied() {
    	DeleteFile service = new DeleteFile(_token, "..");
        service.execute();
    }
    
    @Test(expected = SpecialDirectoriesException.class)
    public void deleteDotPermissionDenied() {
    	DeleteFile service = new DeleteFile(_token, ".");
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteOtherUsersFile() {
    	Directory d = Manager.getInstance().getSessionByToken(_token).getCurrentDirectory();    	
    	Manager.getInstance().getSessionByToken(_token2).setCurrentDirectory(d);
    	DeleteFile service2 = new DeleteFile(_token2, _dirName);
        service2.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteRootDirectoryByUser() {
    	Directory d = (Directory) _user.getFileByPath("/");    	
    	Manager.getInstance().getSessionByToken(_token).setCurrentDirectory(d);
    	DeleteFile service = new DeleteFile(_token, "/");
        service.execute();
    }
    
    @Test(expected = DeleteRootDirectoryException.class)
    public void deleteRootDirectoryByRoot() {
    	Directory d = (Directory) _user.getFileByPath("/");    	
    	Manager.getInstance().getSessionByToken(_tokenroot).setCurrentDirectory(d);
    	DeleteFile service = new DeleteFile(_tokenroot, "/");
        service.execute();
    }
    
    @Override
    public MyDriveService CreateService(String token) {
        return new DeleteFile(token, _dirName);
    }
}