package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
    private Directory _dir2;
    private App _app;
    private App _appRoot;
    private Link _link;
	private static final String _dirName = "Documents";
	private static final String _dirName2 = "dirWithRootFile";
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
	private Session _sessionroot;
	private Session _session;
	private Session _session2;
	
    protected void populate() {
		_user = createUser(_username, _password, _name, _umask);
		_user2 = createUser(_username2, _password2, _name2, _umask);
		_token = createSession(_username, _password);
		_token2 = createSession(_username2, _password2);
		_tokenroot = createSession("root", "***");
		_sessionroot = Manager.getInstance().getSessionByToken(_tokenroot);
		_session = Manager.getInstance().getSessionByToken(_token);
		_session2 = Manager.getInstance().getSessionByToken(_token2);
		_dir = new Directory(_user, _dirName, _session.getCurrentDirectory(), Manager.getInstance());
		_app = new App(_user, _appName, " ", _session.getCurrentDirectory(), Manager.getInstance());
		_dir2 = new Directory(_user, _dirName2, _session.getCurrentDirectory(), Manager.getInstance());
		_link = _user.createLink(_dir, _linkName, _session.getCurrentDirectory(), Manager.getInstance());
		_appRoot = new App(Manager.getInstance().getUserByToken(_tokenroot), "AppRoot", " ", _dir2, Manager.getInstance());
    }

    @Test
    public void successDirByUser() {
        DeleteFile service = new DeleteFile(_token, _dirName);
        service.execute();
        // check dir was deleted
        assertNull("Directory was not deleted", _session.getCurrentDirectory().search(_dirName, _token));
    }
    
    @Test
    public void successAppByUser() {
        DeleteFile service = new DeleteFile(_token, _appName);
        service.execute();
        // check app was deleted
        assertNull("App was not deleted", _session.getCurrentDirectory().search(_appName, _token));
    }
	
    @Test
    public void successLinkByUser() {
        DeleteFile service = new DeleteFile(_token, _linkName);
        service.execute();
        // check link was deleted
        assertNull("Link was not deleted", _session.getCurrentDirectory().search(_linkName, _token));
    }
    
    @Test
    public void successDirByRoot() {
    	Directory d = _session.getCurrentDirectory();    	
    	_sessionroot.setCurrentDirectory(d);
        DeleteFile service = new DeleteFile(_tokenroot, _dirName);
        service.execute();        
        // check dir was deleted
        assertNull("Directory was not deleted by root", d.search(_dirName, _tokenroot));
    }
    
    @Test
    public void successAppByRoot() {
    	Directory d = _session.getCurrentDirectory();    	
    	_sessionroot.setCurrentDirectory(d);
        DeleteFile service = new DeleteFile(_tokenroot, _appName);
        service.execute();
        // check app was deleted
        assertNull("App was not deleted by root", d.search(_appName, _tokenroot));
    }
	
    @Test
    public void successLinkByRoot() {
    	Directory d = _session.getCurrentDirectory();    	
    	_sessionroot.setCurrentDirectory(d);
        DeleteFile service = new DeleteFile(_tokenroot, _linkName);
        service.execute();
        // check link was deleted
        assertNull("Link was not deleted by root", d.search(_linkName, _tokenroot));
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
    	Directory d = _session.getCurrentDirectory();    	
    	_session2.setCurrentDirectory(d);
    	DeleteFile service2 = new DeleteFile(_token2, _dirName);
        service2.execute();
    }
    
    @Test(expected = DeleteRootDirectoryException.class)
    public void deleteRootDirectoryByUser() {
    	Directory d = (Directory) _user.getFileByPath("/", _token);
    	_session.setCurrentDirectory(d);
    	DeleteFile service = new DeleteFile(_token, "/");
        service.execute();
    }
    
    @Test(expected = DeleteRootDirectoryException.class)
    public void deleteRootDirectoryByRoot() {
    	Directory d = (Directory) _user.getFileByPath("/", _tokenroot);
    	_sessionroot.setCurrentDirectory(d);
    	DeleteFile service = new DeleteFile(_tokenroot, "/");
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteDirWithRootFile() {
    	 DeleteFile service = new DeleteFile(_token, _dirName2);
         service.execute();
         // check link was deleted
         assertNotNull("File cannot be deleted", _session.getCurrentDirectory().search(_dirName2, _token));
    }
    
    @Override
    public MyDriveService CreateService(String token) {
        return new DeleteFile(token, _dirName);
    }
}