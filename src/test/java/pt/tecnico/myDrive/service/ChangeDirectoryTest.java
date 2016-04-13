package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.InvalidNameFileException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.service.ChangeDirectory;

public class ChangeDirectoryTest extends TokenVerificationTest{

    private  Directory _dirTest;
    private static final String _username = "Dovah";
    private static final String _password = "Khin";
    private static final String _username2 = "Nathan";
    private static final String _password2 = "Drake";
    private User _user;
    private User _user2;
    private String _token;
    private String _token2;
    private Session s;
    private Session rootSession;
    private String _rootToken;
    private Manager m;
    private static final String giantName = new String(new char[1024]).replace("\0", "1024 caracteres");
    private static final String dirName [] = {
            "games",
            "steam",
            "Fallout",
            "Isaac",
            "Binding",
            "Dark",
            giantName,
            "lol",
            "Malamar",
            "Tormenta"
        };
    private static final String paths [] = {
            "/",
            "/home",
            "/home/Dovah/games",
            "/home/Dovah",
            "//"
        };
    protected void populate(){
        _user = createUser(_username, _password, "DragonBorn",(short) 0xF0);
        _user2 = createUser(_username2, _password2, "Uncharted",(short) 0xF0);
        _token = createSession(_username);
        _token2 = createSession(_username2);
        m = Manager.getInstance();
        s = m.getSessionByToken(_token);
        _rootToken = createSession("root");
        rootSession = m.getSessionByToken(_rootToken);
        int n = 0;
        
        for (int i=0; i<(dirName.length/2 - 1) ; i++) {
        	String direcName = dirName[i];
        	_dirTest = new Directory(_user , direcName, (Directory)m.getUserByToken(_token).getHome(), m);
        }
        String direcName2 = dirName[4];
        _dirTest = new Directory(_user2 , direcName2, (Directory)m.getUserByToken(_token2).getHome(), m);
        for (int j=5; j < (dirName.length -1) ; j++) {
        	String direcName = dirName[j];
        	String path = paths[n];
        	_dirTest = new Directory(m.getUserByToken(_rootToken) , direcName, (Directory)m.getUserByToken(_rootToken).getFileByPath(path), m);
        	n++;
        }
        new PlainFile(m.getUserByToken(_token) , "oneAboveAll", "estoirar o cao", (Directory)m.getUserByToken(_token).getHome(), m);
    }

    @Test
    public void failedChange() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_token, "/home/Dovah/games");
    	FullIvt.execute();
    	s = m.getSessionByToken(FullIvt.result());
    	assertTrue("nao mudou corretamente de diretorio", s.getCurrentDirectory().getName().equals("games"));
    }

    @Test
    public void failedPartialPath() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_token, "games/lol");
    	FullIvt.execute();
    	s = m.getSessionByToken(FullIvt.result());
    	assertTrue("nao mudou corretamente de diretorio", s.getCurrentDirectory().getName().equals("lol"));
    }

    @Test
    public void rootNoChange() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, "/home/Dovah/Fallout");
    	FullIvt.execute();
    	rootSession = m.getSessionByToken(FullIvt.result());
    	assertTrue("root sem permissoes?", rootSession.getCurrentDirectory().getName().equals("Fallout"));
    }

    @Test
    public void couldNotChangeRootDir() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_token, "/Dark");
    	FullIvt.execute();
    	s = m.getSessionByToken(FullIvt.result());
    	assertTrue("user nao conseguiu alterar um diretorio do root", s.getCurrentDirectory().getName().equals("Dark"));
    }

    @Test
    public void dotPathFail() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, ".");
    	String prechange = rootSession.getCurrentDirectory().getName();
    	FullIvt.execute();
    	rootSession = m.getSessionByToken(FullIvt.result());
    	assertTrue("nao mudou corretamente para ele proprio", prechange.equals(rootSession.getCurrentDirectory().getName()));
    }

    
    @Test
    public void dotDotPathFail() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, "..");
    	String prechange = rootSession.getCurrentDirectory().getParent().getName();
    	FullIvt.execute();
    	rootSession = m.getSessionByToken(FullIvt.result());
    	assertTrue("nao mudou corretamente para o pai", rootSession.getCurrentDirectory().getName().equals(prechange));
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFile(){
        ChangeDirectory Boom = new ChangeDirectory(_rootToken , "/voidBorn");
        Boom.execute();
    }

    @Test(expected = FileNotFoundException.class)
    public void noPath(){
        ChangeDirectory Boom = new ChangeDirectory(_rootToken , "");
        Boom.execute();
    }
    
    @Test(expected = FileNotFoundException.class)
    public void invalidPartialPath(){
        ChangeDirectory Boom = new ChangeDirectory(_rootToken , "balelas/outrasbalelas");
        Boom.execute();
    }
    
    @Test(expected = InvalidNameFileException.class)
    public void tooMuchChars() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, "/home/" + giantName);
    	FullIvt.execute();
    }
    
    @Test(expected = InvalidFileTypeException.class)
    public void wrongFileType() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, "/home/Dovah/oneAboveAll");
    	FullIvt.execute();
    }

    @Test(expected = ReadPermissionException.class)
    public void surpassedPermission(){
        ChangeDirectory Boom = new ChangeDirectory(_token , "/home/Nathan/Binding");
        Boom.execute();
    }

    @Override
    public MyDriveService CreateService(String token) {
        return new ChangeDirectory(token, "/");
    }
}
