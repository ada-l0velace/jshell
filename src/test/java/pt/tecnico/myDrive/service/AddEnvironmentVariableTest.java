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
    private static final String _password = "KhinKhin";
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
        EnvironmentVariableDto newVariavel = new EnvironmentVariableDto("$cenas", "coisas");
    }

    @Test
    public void success() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "$urso", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	for(int i = 0; i<aev.size();i ++){
    		if(aev.get(i).getName().equals("$urso")){
    			varCheck = true;
    		}
    	}
    	assertTrue("nao adicionou corretamente a variavel", varCheck);
    }
    
    @Test
    public void rootSuccess() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "$urso", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	for(int i = 0; i < aev.size();i ++){
    		if(aev.get(i).getName().equals("$urso")){
    			varCheck = true;
    		}
    	}
    	assertTrue("nao adicionou corretamente a variavel com o root", varCheck);
    }
    
    @Test
    public void reDefineSuccess() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "$cenas", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	for(int i = 0; i < aev.size();i ++){
    		if(aev.get(i).getName().equals("$cenas") && aev.get(i).getValue().equals("banana")){
    			varCheck = true;
    		}
    	}
    	assertTrue("nao redefiniu bem a variavel", varCheck);
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
    
    @Test(expected = EmptyVariableValueException.class)
    public void noContentRedefine() {
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "$cenas", "");
    	EVS.execute();
    }
    

    @Override
    public MyDriveService CreateService(String token) {
        return new ChangeDirectoryService(token, "/");
    }
}
