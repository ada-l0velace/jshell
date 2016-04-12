package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;
import pt.tecnico.myDrive.exception.FileNotFoundException;

public class ChangeDirectoryTest extends TokenVerificationTest{

    private static final String _dirTest = "changeTester";
    private static final String _username = "Dovah";
    private static final String _password = "Khin";
    private User _user;
    private String _token;
    private Session s;
    private static final String dirName [] = {
            "games",
            "steam",
            "Fallout",
            "Isaac",
            "Binding",
            "Dark",
            "Souls",
            "lol",
            "Malamar"
        };
    private static final String paths [] = {
            "/",
            "/home",
            ".",
            "..",
            "/home/Dovah",
            "/home/",
            "/home/Dovah/",
            "/",
            "//"
        };
    protected void populate(){
        _user = createUser(_username, _password, "DragonBorn",(short) 255);
        _token = createSession(username);
        
        for (String username : dirName && String path : paths) {
        	_dirTest = createDir(_user , dirName, paths, Manager.getInstance());
        }
    }

    @Test
    public void emptyChange() {
    	
    }
    @Test
    public void success(){
    	ChangeDirectory FullIvt = ChangeDirectory(log.result() , IventPath);
        FullIvt.execute();
        
        assertFalse("O ficheiro nao foi trocado", s.getCurrentDirectory().getName().equals(IventPath));
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFile(){
        ChangeDirectory Boom = ChangeDirectory(log.result() , "/voidBorn");
        FullIvt.execute();
    }
    
    @Override
    public MyDriveService CreateService(String token) {
        return new ListDirectory(token);
    }
}
