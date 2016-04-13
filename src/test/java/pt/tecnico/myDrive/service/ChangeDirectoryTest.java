package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.service.ChangeDirectory;

public class ChangeDirectoryTest extends TokenVerificationTest{

    private  Directory _dirTest;
    private static final String _username = "Dovah";
    private static final String _password = "Khin";
    private User _user;
    private String _token;
    private Session s;
    private String _rootToken;
    private static final String dirName [] = {
            "games",
            "steam",
            "Fallout",
            "Isaac",
            "Binding",
            "Dark",
            "Souls",
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
        _user = createUser(_username, _password, "DragonBorn",(short) 255);
        _token = createSession(_username);
        Manager m = Manager.getInstance();
        s = m.getSessionByToken(_token);
        _rootToken = createSession("root");
        
        for (int i=0; i<(dirName.length/2) ; i++) {
        	String direcName = dirName[i];
        	_dirTest = new Directory(_user , direcName, (Directory)m.getUserByToken(_token).getHome(), m);
        }
        for (int j=5; j < (dirName.length -1) ; j++) {
        	int n = 0;
        	String direcName = dirName[j];
        	String path = paths[n];
        	_dirTest = new Directory(m.getUserByToken(_rootToken) , direcName, (Directory)m.getUserByToken(_rootToken).getFileByPath(path), m);
        	n++;
        }
    }

    @Test
    public void emptyChange() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken , "");
    	String prechange = s.getCurrentDirectory().getName();
    	FullIvt.execute();
    	assertFalse("mudou para um diretorio com path vazio", prechange.equals(""));
    }
    
    @Test
    public void failedChange() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_token, "/home/Dovah/games");
    	FullIvt.execute();
    	assertFalse("nao mudou corretamente de diretorio", s.getCurrentDirectory().getName().equals("games"));
    }
    
    @Test
    public void rootNoChange() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, "/Dark");
    	FullIvt.execute();
    	assertFalse("root sem permissoes?", s.getCurrentDirectory().getName().equals("Dark"));
    }
    
    @Test
    public void dotPathFail() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, ".");
    	String prechange = s.getCurrentDirectory().getName();
    	FullIvt.execute();
    	assertFalse("nao mudou corretamente para ele proprio", prechange.equals(s.getCurrentDirectory().getName()));
    }
    
    
    @Test
    public void dotDotPathFail() {
    	ChangeDirectory FullIvt = new ChangeDirectory(_rootToken, "..");
    	String prechange = s.getCurrentDirectory().getName();
    	FullIvt.execute();
    	assertFalse("nao mudou corretamente para o pai", s.getCurrentDirectory().getParent().getName().equals(prechange));
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFile(){
        ChangeDirectory Boom = new ChangeDirectory(_rootToken , "/voidBorn");
        Boom.execute();
    }
    
    @Override
    public MyDriveService CreateService(String token) {
        return new ChangeDirectory(token, "/");
    }
}
