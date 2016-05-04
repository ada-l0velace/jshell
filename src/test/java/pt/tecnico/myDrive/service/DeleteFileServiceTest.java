package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
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
import pt.tecnico.myDrive.service.factory.Factory;
import pt.tecnico.myDrive.service.factory.FileFactoryProducer;


public class DeleteFileServiceTest extends TokenVerificationTest {
	
    private Directory _dir;
    private Directory _dir1;
    private Directory _dir2;
    private App _app;
    private App _app1;
    private App _appRoot;
    private Link _link;
	private static final String _dirName = "Empty";
	private static final String _dirName1 = "Documents";
	private static final String _dirName2 = "dirWithRootFile";
	private static final String _appName = "App";
	private static final String _appName1 = "App1";
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
		_dir1 = new Directory(_user, _dirName1, _session.getCurrentDirectory(), Manager.getInstance());
		_app = new App(_user, _appName, " ", _session.getCurrentDirectory(), Manager.getInstance());
		_dir2 = new Directory(_user, _dirName2, _session.getCurrentDirectory(), Manager.getInstance());
		_app1 = new App(_user, _appName1, " ", _dir1, Manager.getInstance());
		_link = _user.createLink(_dir, _linkName, _session.getCurrentDirectory(), Manager.getInstance());
		_appRoot = new App(Manager.getInstance().getUserByToken(_tokenroot), "AppRoot", " ", _dir2, Manager.getInstance());
    }

    @Test
    public void successDirWithContentByUser() {
        DeleteFileService service = new DeleteFileService(_token, _dirName1);
        service.execute();
        // check dir was deleted
        assertNull("Directory was not deleted", _session.getCurrentDirectory().searchFile(_dirName1, _token));
    }
    
    @Test
    public void successDirEmptyByUser() {
        DeleteFileService service = new DeleteFileService(_token, _dirName);
        service.execute();
        // check dir was deleted
        assertNull("Directory was not deleted", _session.getCurrentDirectory().searchFile(_dirName, _token));
    }
    
    @Test
    public void successAppByUser() {
        DeleteFileService service = new DeleteFileService(_token, _appName);
        service.execute();
        // check app was deleted
        assertNull("App was not deleted", _session.getCurrentDirectory().searchFile(_appName, _token));
    }
	
    @Test
    public void successLinkByUser() {
        DeleteFileService service = new DeleteFileService(_token, _linkName);
        service.execute();
        // check link was deleted
        assertNull("Link was not deleted", _session.getCurrentDirectory().searchFile(_linkName, _token));
    }
        
    @Test
    public void successDirEmptyByRoot() {
    	Directory d = _session.getCurrentDirectory();    	
    	_sessionroot.setCurrentDirectory(d);
        DeleteFileService service = new DeleteFileService(_tokenroot, _dirName);
        service.execute();        
        // check dir was deleted
        assertNull("Directory was not deleted by root", d.searchFile(_dirName, _tokenroot));
    }
    
    @Test
    public void successDirWithContentByRoot() {
    	Directory d = _session.getCurrentDirectory();    	
    	_sessionroot.setCurrentDirectory(d);
        DeleteFileService service = new DeleteFileService(_tokenroot, _dirName1);
        service.execute();        
        // check dir was deleted
        assertNull("Directory was not deleted by root", d.searchFile(_dirName1, _tokenroot));
    }
    
    @Test
    public void successAppByRoot() {
    	Directory d = _session.getCurrentDirectory();    	
    	_sessionroot.setCurrentDirectory(d);
        DeleteFileService service = new DeleteFileService(_tokenroot, _appName);
        service.execute();
        // check app was deleted
        assertNull("App was not deleted by root", d.searchFile(_appName, _tokenroot));
    }
	
    @Test
    public void successLinkByRoot() {
    	Directory d = _session.getCurrentDirectory();    	
    	_sessionroot.setCurrentDirectory(d);
        DeleteFileService service = new DeleteFileService(_tokenroot, _linkName);
        service.execute();
        // check link was deleted
        assertNull("Link was not deleted by root", d.searchFile(_linkName, _tokenroot));
    }
    
    @Test(expected = FileNotFoundException.class)
    public void deleteNonExistingFile() {
        DeleteFileService service = new DeleteFileService(_token, "Images");
        service.execute();
    }
    
    @Test(expected = SpecialDirectoriesException.class)
    public void deleteDotDotPermissionDenied() {
    	DeleteFileService service = new DeleteFileService(_token, "..");
        service.execute();
    }
    
    @Test(expected = SpecialDirectoriesException.class)
    public void deleteDotPermissionDenied() {
    	DeleteFileService service = new DeleteFileService(_token, ".");
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteOtherUsersFile() {
    	Directory d = _session.getCurrentDirectory();    	
    	_session2.setCurrentDirectory(d);
    	DeleteFileService service2 = new DeleteFileService(_token2, _dirName);
        service2.execute();
    }
    
    @Test(expected = DeleteRootDirectoryException.class)
    public void deleteRootDirectoryByUser() {
    	Directory d = (Directory) _user.getFileByPath("/", _token);
    	_session.setCurrentDirectory(d);
    	DeleteFileService service = new DeleteFileService(_token, "/");
        service.execute();
    }
    
    @Test(expected = DeleteRootDirectoryException.class)
    public void deleteRootDirectoryByRoot() {
    	Directory d = (Directory) _user.getFileByPath("/", _tokenroot);
    	_sessionroot.setCurrentDirectory(d);
    	DeleteFileService service = new DeleteFileService(_tokenroot, "/");
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteDirWithRootFile() {
    	 DeleteFileService service = new DeleteFileService(_token, _dirName2);
         service.execute();
         // check link was deleted
         assertNotNull("File cannot be deleted", _session.getCurrentDirectory().searchFile(_dirName2, _token));
    }
    
    @Test(expected = FileNotFoundException.class)
    public void deleteCurrentDirByUser() {
        DeleteFileService service = new DeleteFileService(_token, _session.getCurrentDirectory().getName());
        service.execute();
    }

    @Test
    public void deleteCurrentDir() {
        Factory fileFactory = FileFactoryProducer.getFactory(Factory.FileType.DIRECTORY, _token);
        Directory d = (Directory) fileFactory.CreateFile("NewDir", "");
        _sessionroot.setCurrentDirectory(_session.getCurrentDirectory());
        _session.setCurrentDirectory(d);
        DeleteFileService service = new DeleteFileService(_tokenroot, "NewDir");
        service.execute();
        String currentDirName = _session.getCurrentDirectory().getName();
        String homeDirName = _session.getUser().getHome().getName();
        assertEquals("Current directory wasn't set to home", currentDirName, homeDirName);
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteFileWithoutParentWriteFlagOn() {
    	_dir1.getPermissions().setUmask((short)0xF9);
    	_app1.getPermissions().setUmask((short)0xF9);
    	_session2.setCurrentDirectory(_dir1);
        DeleteFileService service = new DeleteFileService(_token2, _appName1);
        service.execute();
    }

    @Test(expected = DeletePermissionException.class)
    public void deleteFileWithoutDeleteFlagOn() {
        _dir1.getPermissions().setUmask((short)0xFC);
        _app1.getPermissions().setUmask((short)0xF8);
        _session2.setCurrentDirectory(_dir1);
        DeleteFileService service = new DeleteFileService(_token2, _appName1);
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteFileWithoutWriteParent() {
    	Directory d = _session.getCurrentDirectory();    	
    	d.getPermissions().setUmask((short)0xB0);
    	DeleteFileService service = new DeleteFileService(_token, _appName);
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteFileWithoutDeletePermission() {
    	Directory d = _session.getCurrentDirectory();    	
    	_app.getPermissions().setUmask((short)0xE0);
    	DeleteFileService service = new DeleteFileService(_token, _appName);
        service.execute();
    }
    
    @Test(expected = DeletePermissionException.class)
    public void deleteWithoutDeletePermissionWriteParent() {
    	Directory d = _session.getCurrentDirectory();    	
    	d.getPermissions().setUmask((short)0xB0);
    	_app.getPermissions().setUmask((short)0xE0);
    	DeleteFileService service = new DeleteFileService(_token, _appName);
        service.execute();
    }
    
    @Override
    public MyDriveService CreateService(String token) {
        return new DeleteFileService(token, "Testdoc");
    }
}
