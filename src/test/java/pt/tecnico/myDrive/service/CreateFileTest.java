package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.DirectoryContentException;
import pt.tecnico.myDrive.exception.LinkEmptyContentException;
import static org.junit.Assert.assertEquals;

public class CreateFileTest extends TokenVerificationTest {
	
    private Directory _dir;
    private String _username = "rikku";
    private String _filename = "heartless";
    private String _content = "OrganizationXIII rules!!!";
    private String _token;
    private User _user;
    private Manager m;
    
    protected void populate() {
        _user = createUser(_username, "keyblademaster", "Sora", (short) 255);
        _token = createSession(_username);
        m = Manager.getInstance();
        _dir = new Directory();
    }

    @Test
    public void createAppWithoutContent(){
        CreateFile service = new CreateFile(_token, _filename, "A");
        service.execute();
    }

    @Test
    public void createAppWithContent(){
        CreateFile service = new CreateFile(_token, _filename, "A", _content);
        service.execute();
    }

    @Test
    public void createPlainfileWithoutContent(){
        CreateFile service = new CreateFile(_token, _filename, "P");
        service.execute();
    }

    @Test
    public void createPlainfileWithContent(){
        CreateFile service = new CreateFile(_token, _filename, "P", _content);
        service.execute();
    }

    @Test
    public void createDirectoryWithoutContent(){
        CreateFile service = new CreateFile(_token, _filename, "D");
        service.execute();
    }

    @Test(expected = DirectoryContentException.class)
    public void createDirectoryWithContent(){
        CreateFile service = new CreateFile(_token, _filename, "D", _content);
        service.execute();
    }

    @Test
    public void createLinkWithPath() {
        CreateFile service = new CreateFile(_token, _filename, "L", "/home");
        service.execute();
    }

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
        CreateFile service = new CreateFile(_token, _filename, "A", "myContent");
        service.execute();
        assertEquals(m.getUserByToken(_token).getPermissions(), m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename).getPermissions());
    }
                        
    @Override
    public MyDriveService CreateService(String token) {
        return new CreateFile(token, "somethingfile", "L");
    }


}
