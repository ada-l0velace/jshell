package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
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
import pt.tecnico.myDrive.presentation.Sys;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.InvalidVariableNameException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.EmptyVariableValueException;
import pt.tecnico.myDrive.exception.EnvVarNameNotFoundException;
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
        EnvironmentVariable newVar = new EnvironmentVariable("cenas", "banana", m.getSessionByToken(_token));
    }

    @Test
    public void success() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "urso", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	for(int i = 0; i<aev.size();i ++){
    		if(aev.get(i).getName().equals("urso")){
    			varCheck = true;
    		}
    	}
    	assertTrue("nao adicionou corretamente a variavel", varCheck);
    }
    
    @Test
    public void rootSuccess() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "urso", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	for(int i = 0; i < aev.size();i ++){
    		if(aev.get(i).getName().equals("urso")){
    			varCheck = true;
    		}
    	}
    	assertTrue("nao adicionou corretamente a variavel com o root", varCheck);
    }
    
    @Test
    public void reDefineSuccess() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "cenas", "banana");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	for(int i = 0; i < aev.size();i ++){
    		if(aev.get(i).getName().equals("cenas") && aev.get(i).getValue().equals("banana")){
    			varCheck = true;
    		}
    	}
    	assertTrue("nao redefiniu bem a variavel", varCheck);
    }
    
    @Test
    public void emptyValueSuccess() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "cenas", "");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	for(int i = 0; i < aev.size();i ++){
    		if(aev.get(i).getName().equals("cenas")){
    			varCheck = true;
    		}
    	}
    	assertTrue("nao executou corretamente com valor vazio", varCheck);
    }
    
    @Test
    public void emptyNameValueSuccess() {
    	boolean varCheck = false;
    	EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "", "");
    	EVS.execute();
    	List<EnvironmentVariableDto> aev = EVS.result();
    	if (aev.size() != 0)
    		varCheck = true;
    	assertTrue("nao executou corretamente com valor e nome vazio", varCheck);
    }

	@Test(expected = EnvVarNameNotFoundException.class)
	public void invalidName() {
		EnvironmentVariableService EVS = new EnvironmentVariableService(_token, "balelas");
		EVS.execute();
	}

	@Test
	public void outputOneEnv() {
        EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "balelas", "hehe");
		EnvironmentVariableService EVS1 = new EnvironmentVariableService(_rootToken, "balelas");
		EVS.execute();
        EVS1.execute();
        assertTrue(EVS.output().equals(""));
        assertTrue(EVS1.output().equals("hehe\n"));
	}

    @Test
    public void outputOneMulEnv() {
        EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "balelas", "hehe");
        EnvironmentVariableService EVS1 = new EnvironmentVariableService(_rootToken);
        EVS.execute();
        EVS1.execute();
        assertTrue(EVS.output().equals(""));
        assertTrue(EVS1.output().equals("$balelas=hehe\n"));
    }

	@Test
	public void outputMulEnv() {
		EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken, "balelas", "hehe");
		EnvironmentVariableService EVS1 = new EnvironmentVariableService(_rootToken, "poodle", "hehe");
        EnvironmentVariableService EVS3 = new EnvironmentVariableService(_rootToken);
        EVS.execute();
		EVS1.execute();
        EVS3.execute();
		assertTrue(EVS.output().equals(""));
        assertTrue(EVS1.output().equals(""));
        assertTrue(EVS3.output().equals("$poodle=hehe, $balelas=hehe\n"));
    }

	@Test
	public void outputEmpty() {
		EnvironmentVariableService EVS = new EnvironmentVariableService(_rootToken);
		EVS.execute();
        assertTrue(EVS.output().equals("\n"));
	}


    @Override
    public MyDriveService CreateService(String token) {
        return new ChangeDirectoryService(token, "/");
    }
}
