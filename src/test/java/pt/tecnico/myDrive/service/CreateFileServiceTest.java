package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.exception.*;
import pt.tecnico.myDrive.service.factory.Factory.FileType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateFileServiceTest extends TokenVerificationTest {

    private Directory _dir;
    private String _username = "roxas";
    private String _filename = "heartless";
    private String _content = "OrganizationXIII rules!!!";
    private String _token;
    private String _rootToken;
    private String _worldRToken;
    private String _worldWToken;
    private Manager m;

    private static final String USERNAME [] = {
            "roxas",
            "thewiseone",
            "notansem"
    };

    private static final String PASSWORD [] = {
            "keyblademaster",
            "whatailsyou",
            "birthbysleep"
    };

    private static final String NAME [] = {
            "Sora",
            "Ansem",
            "Xehanort"
    };

    protected void populate() {
        createUser(USERNAME[0], PASSWORD[0], NAME[0], (short) 0xFF);
        createUser(USERNAME[1], PASSWORD[1], NAME[1], (short) 0x88);
        createUser(USERNAME[2], PASSWORD[2], NAME[2], (short) 0xFF);
        _token = createSession(USERNAME[0], PASSWORD[0]);
        _rootToken = createSession("root", "***");
        _worldRToken = createSession(USERNAME[1], PASSWORD[1]);
        _worldWToken = createSession(USERNAME[2], PASSWORD[2]);
        m = Manager.getInstance();
    }

    @Test
    public void createAppWithoutContent(){
        CreateFileService service = new CreateFileService(_token, _filename, FileType.APP);
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename, _token));
    }

    @Test
    public void createAppWithContent(){
        CreateFileService service = new CreateFileService(_token, _filename, FileType.APP, _content);
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename, _token));}

    @Test
    public void createPlainfileWithoutContent(){
        CreateFileService service = new CreateFileService(_token, _filename, FileType.PLAINFILE);
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename, _token));}

    @Test
    public void createPlainfileWithContent(){
        CreateFileService service = new CreateFileService(_token, _filename, FileType.PLAINFILE, _content);
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename, _token));}

    @Test
    public void createDirectoryWithoutContent(){
        CreateFileService service = new CreateFileService(_token, _filename, FileType.DIRECTORY);
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename, _token));}

    @Test(expected = DirectoryContentException.class)
    public void createDirectoryWithContent(){
        CreateFileService service = new CreateFileService(_token, _filename, FileType.DIRECTORY, _content);
        service.execute();
    }

    @Test
    public void createLinkWithPath() {
        CreateFileService service = new CreateFileService(_token, _filename, FileType.LINK, "/home");
        service.execute();
        assertNotNull(m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename, _token));}

    @Test(expected = FileNotFoundException.class)
    public void createLinkWithPathWithoutFile() {
        CreateFileService service = new CreateFileService(_token, _filename, FileType.LINK, _content);
        service.execute();
    }

    @Test(expected = NameFileAlreadyExistsException.class)
    public void createAlreadyExistingFile() {
        createFile(FileType.PLAINFILE,_token, _filename);
        CreateFileService service = new CreateFileService(_token, _filename, FileType.PLAINFILE);
        service.execute();
    }

    @Test(expected = LinkEmptyContentException.class)
    public void createLinkWithoutContent() {
        CreateFileService service = new CreateFileService(_token, _filename, FileType.LINK);
        service.execute();
    }

    @Test(expected = InvalidNameFileException.class)
    public void createFileWithSlashInName() {
        CreateFileService service = new CreateFileService(_token, "/", FileType.PLAINFILE);
        service.execute();
    }

    @Test(expected = InvalidNameFileException.class)
    public void createFileWithNullInName() {
        CreateFileService service = new CreateFileService(_token, "\0", FileType.PLAINFILE);
        service.execute();
    }

    @Test
    public void fileWithDifferentPermissionsThanUser() {
        CreateFileService service = new CreateFileService(_token, _filename, FileType.APP, _content);
        service.execute();
        assertEquals(m.getUserByToken(_token).getPermissions().getUmask(), m.getSessionByToken(_token).getCurrentDirectory().searchFile(_filename, _token).getPermissions().getUmask());
    }

    @Test(expected = WritePermissionException.class)
    public void createFileWithoutPermissions(){
        CreateFileService service = new CreateFileService(_worldRToken, _filename, FileType.APP, _content);
        service.execute();
    }

    @Test(expected = InvalidNameFileException.class)
    public void createFileNameToLong(){
        String bigName = new String(new char[1024]).replace("\0", "1024 caracteres");
        CreateFileService service = new CreateFileService(_token, bigName, FileType.PLAINFILE);
        service.execute();
    }

    @Test
    public void worldCanCreateFile(){
        CreateFileService service = new CreateFileService(_worldWToken, _filename, FileType.DIRECTORY);
        service.execute();
        assertNotNull(m.getSessionByToken(_worldWToken).getCurrentDirectory().searchFile(_filename, _worldWToken));
    }

    @Test
    public void superUserCanCreateFile(){        
        Session s = m.getSessionByToken(_rootToken);
        s.setCurrentDirectory(m.getSessionByToken(_worldWToken).getCurrentDirectory());
        CreateFileService service = new CreateFileService(_rootToken, _filename, FileType.DIRECTORY);
        service.execute();
        assertNotNull(m.getSessionByToken(_worldWToken).getCurrentDirectory());
    }

    @Test(expected = InvalidFileTypeException.class)
    public void wrongFileType(){
        CreateFileService service = new CreateFileService(_worldWToken, _filename, FileType.UNKNOWN);
        service.execute();
    }

    @Override
    public MyDriveService CreateService(String token) {
        return new CreateFileService(token, "somefile", FileType.APP);
    }
}
