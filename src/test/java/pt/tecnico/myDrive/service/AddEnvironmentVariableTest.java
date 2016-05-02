package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.InvalidVariableNameException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.EmptyVariableValueException;
import java.util.List;

public class AddEnvironmentVariableTest extends TokenVerificationTest{

    private  Directory _dirTest;
    private static final String _username = "Dovah";
    private static final String _password = "Khin";
    private User _user;
    private String _token;
    private Session s;
    private Session rootSession;
    private String _rootToken;
    private Manager m;

    protected void populate(){
        _user = createUser(_username, _password, "DragonBorn",(short) 0xF0);
        _token = createSession(_username, _password);
        m = Manager.getInstance();
        s = m.getSessionByToken(_token);
        _rootToken = createSession("root", "***");
        rootSession = m.getSessionByToken(_rootToken);
    }

    @Test
    public void success() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "$urso", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	/*for(int i = 0; i<aev.size();i ++){
    		if(aev.get(i).getName().equals("$urso")){
    			varCheck = true;
    		}
    	}*/
    	assertTrue("nao adicionou corretamente a variavel", varCheck);
    }
    
    @Test
    public void rootSuccess() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "$urso", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	/*for(int i = 0; i < aev.size();i ++){
    		if(aev.get(i).getName().equals("$urso")){
    			varCheck = true;
    		}
    	}*/
    	assertTrue("nao adicionou corretamente a variavel com o root", varCheck);
    }

    @Test(expected = InvalidVariableNameException.class)
    public void invalidName() {
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "", "banana");
    	EVS.execute();
    }
    @Test(expected = EmptyVariableValueException.class)
    public void noContent() {
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "$urso", "");
    	EVS.execute();
    }
    
    /*@Test
    public void failedPartialPath() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_token, "games/lol");
    	FullIvt.execute();
    	s = m.getSessionByToken(FullIvt.result());
    	assertTrue("nao mudou corretamente de diretorio", s.getCurrentDirectory().getName().equals("lol"));
    }

    @Test
    public void rootNoChange() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_rootToken, "/home/Dovah/Fallout");
    	FullIvt.execute();
    	rootSession = m.getSessionByToken(FullIvt.result());
    	assertTrue("root sem permissoes?", rootSession.getCurrentDirectory().getName().equals("Fallout"));
    }

    @Test
    public void couldNotChangeRootDir() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_token, "/Dark");
    	FullIvt.execute();
    	s = m.getSessionByToken(FullIvt.result());
    	assertTrue("user nao conseguiu alterar um diretorio do root", s.getCurrentDirectory().getName().equals("Dark"));
    }

    @Test
    public void dotPathFail() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_rootToken, ".");
    	String prechange = rootSession.getCurrentDirectory().getName();
    	FullIvt.execute();
    	rootSession = m.getSessionByToken(FullIvt.result());
    	assertTrue("nao mudou corretamente para ele proprio", prechange.equals(rootSession.getCurrentDirectory().getName()));
    }

    
    @Test
    public void dotDotPathFail() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_rootToken, "..");
    	String prechange = rootSession.getCurrentDirectory().getParent().getName();
    	FullIvt.execute();
    	rootSession = m.getSessionByToken(FullIvt.result());
    	assertTrue("nao mudou corretamente para o pai", rootSession.getCurrentDirectory().getName().equals(prechange));
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFile(){
        ChangeDirectoryService Boom = new ChangeDirectoryService(_rootToken , "/voidBorn");
        Boom.execute();
    }

    @Test(expected = FileNotFoundException.class)
    public void noPath(){
        ChangeDirectoryService Boom = new ChangeDirectoryService(_rootToken , "");
        Boom.execute();
    }
    
    @Test(expected = FileNotFoundException.class)
    public void invalidPartialPath(){
        ChangeDirectoryService Boom = new ChangeDirectoryService(_rootToken , "balelas/outrasbalelas");
        Boom.execute();
    }
    
    @Test(expected = InvalidNameFileException.class)
    public void tooMuchChars() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_rootToken, "/home/" + giantName);
    	FullIvt.execute();
    }
    
    @Test(expected = InvalidFileTypeException.class)
    public void wrongFileType() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_rootToken, "/home/Dovah/oneAboveAll");
    	FullIvt.execute();
    }
    
    @Test(expected = InvalidFileTypeException.class)
    public void wrongAppType() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_rootToken, "/home/Dovah/appBroker");
    	FullIvt.execute();
    }
    
    @Test(expected = InvalidFileTypeException.class)
    public void wrongLinkType() {
    	ChangeDirectoryService FullIvt = new ChangeDirectoryService(_rootToken, "/home/Dovah/aLinkToTheCrash");
    	FullIvt.execute();
    }

    @Test(expected = ReadPermissionException.class)
    public void surpassedPermission(){
        ChangeDirectoryService Boom = new ChangeDirectoryService(_token , "/home/Nathan/Binding");
        Boom.execute();
    }
*/
    @Override
    public MyDriveService CreateService(String token) {
        return new ChangeDirectoryService(token, "/");
    }
}
