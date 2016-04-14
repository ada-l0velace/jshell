package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.WritePermissionException;
import pt.tecnico.myDrive.exception.DirectoryContentException;
import pt.tecnico.myDrive.exception.LinkEmptyContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateFileTest extends TokenVerificationTest {

    private Directory _dir;
    private String _username = "roxas";
    private String _filename = "heartless";
    private String _content = "OrganizationXIII rules!!!";
    private String _token;
    private String _rootToken;
    private String _worldRToken;
    private String _worldWToken;
    private User _worldRUser;
    private User _worldWUser;
    private User _user;
    private Manager m;
    
    protected void populate() {
        _user = createUser(_username, "keyblademaster", "Sora", (short) 0xFF);
        _worldRUser = createUser("thewiseone", "whatailsyou", "Ansem", (short) 0x44);
        _worldWUser = createUser("notansem", "birthbysleep", "Xehanort", (short) 0xFF);
        _token = createSession(_username);
        _rootToken = createSession("root");
        _worldRToken = createSession("thewiseone");
        _worldWToken = createSession("notansem");
        m = Manager.getInstance();
        
    }

    @Test
    public void createAppWithoutContent(){
        CreateFile service = new CreateFile(_token, _filename, "A");
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename));    
    }

    @Test
    public void createAppWithContent(){
        CreateFile service = new CreateFile(_token, _filename, "A", _content);
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename));}

    @Test
    public void createPlainfileWithoutContent(){
        CreateFile service = new CreateFile(_token, _filename, "P");
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename));}

    @Test
    public void createPlainfileWithContent(){
        CreateFile service = new CreateFile(_token, _filename, "P", _content);
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename));}

    @Test
    public void createDirectoryWithoutContent(){
        CreateFile service = new CreateFile(_token, _filename, "D");
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename));}

    @Test(expected = DirectoryContentException.class)
    public void createDirectoryWithContent(){
        CreateFile service = new CreateFile(_token, _filename, "D", _content);
        service.execute();
    }

    @Test
    public void createLinkWithPath() {
        CreateFile service = new CreateFile(_token, _filename, "L", "/home");
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename));}

    @Test(expected = FileNotFoundException.class)
    public void createLinkWithPathWithoutFile() {
        CreateFile service = new CreateFile(_token, _filename, "L", _content);
        service.execute();
    }

    @Test(expected = LinkEmptyContentException.class)
    public void createLinkWithoutContent() {
        CreateFile service = new CreateFile(_token, _filename, "L");
        service.execute();
    }


    @Test
    public void fileWithDifferentPermissionsThanUser() {
        CreateFile service = new CreateFile(_token, _filename, "A", _content);
        service.execute();
        assertEquals(m.getUserByToken(_token).getPermissions().getUmask(), m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename).getPermissions().getUmask());
    }

    @Test(expected = WritePermissionException.class)
    public void createFileWithoutPermissions(){
        CreateFile service = new CreateFile(_worldRToken, _filename, "A", _content);
        service.execute();
    }

    @Test
    public void worldCanCreateFile(){
        CreateFile service = new CreateFile(_worldWToken, _filename, "D");
        service.execute();
        assertNotNull(m.getSessionByToken(_worldWToken).getCurrentDirectory().searchFile(_filename));
    }

    @Test
    public void superUserCanCreateFile(){        
        Session s = m.getSessionByToken(_rootToken);
        s.setCurrentDirectory(m.getSessionByToken(_worldWToken).getCurrentDirectory());
        CreateFile service = new CreateFile(_rootToken, _filename, "D");
        service.execute();
        assertNotNull(m.getSessionByToken(_worldWToken).getCurrentDirectory().searchFile(_filename));
    }

    @Test(expected = InvalidFileTypeException.class)
    public void wrongFileType(){
        CreateFile service = new CreateFile(_worldWToken, _filename, "J");
        service.execute();
    }

                        
    @Override
    public MyDriveService CreateService(String token) {
        return new CreateFile(token, "somethingfile", "L");
    }


}
