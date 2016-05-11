package pt.tecnico.myDrive.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;

import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.service.dto.*;
import pt.tecnico.myDrive.exception.*;
import pt.tecnico.myDrive.service.factory.Factory.FileType;
import pt.tecnico.myDrive.presentation.Hello;


@RunWith(JMockit.class)
public class IntegrationTest extends BaseServiceTest {

	private static final String _username = "toni", _pass = "tonitoni16";
	private static final String _nameDir1 = "Documents",  _nameDir2= "Games", _nameApp = "App";
	private static final String _path = "/home/toni/Documents";
	private static final String _content = "pt.tecnico.myDrive.presentation.Hello.execute";
	private User _user;
	private String _token;

	protected void populate() { // populate mockup
		_user = createUser(_username, _pass, "Toni", (short) 0xF0);
    }

	
    @Test
    public void success(@Mocked Hello h) throws Exception {

    	LoginUserService login = new LoginUserService(_username, _pass);
        login.execute();
        String token = login.result();
        
        new CreateFileService(token, _nameDir1, FileType.DIRECTORY).execute();
        new CreateFileService(token, _nameDir2, FileType.DIRECTORY).execute();
        new CreateFileService(token, _nameApp, FileType.APP, "").execute();
        
        File app = _user.getFileByPath("/home/toni/App", token);
        
        ListDirectoryService ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 5); // ., .., Documents, Games, App
    	
    	new DeleteFileService(token, _nameDir2).execute();
    	
    	ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 4); //., .., Documents, App
    	
    	new WritePlainFileService(token, _nameApp, _content).execute();
    	
    	ReadPlainFileService rpf = new ReadPlainFileService(token, _nameApp);
    	rpf.execute();    	
        assertEquals(rpf.result(), _content); //"pt.tecnico.myDrive.presentation.Hello.execute"
        
        ChangeDirectoryService cd = new ChangeDirectoryService(token, _path);
        cd.execute();
        
        new CreateFileService(token, _nameDir2, FileType.DIRECTORY).execute();
        
        ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 3); // ., .., Games
    	
    	String [] args = { app.getPath() + app.getName() };
    	ExecuteFileService service = new ExecuteFileService(token, app.getPath() + app.getName(), args);
        service.execute();
        new FullVerifications() { 
        	{
                String [] args = {app.getPath() + app.getName()};
                h.execute(args);
                times = 1;
            }
        };
        
        boolean varCheck = false;
    	EnvironmentVariableService ev = new EnvironmentVariableService(token, "nome", "valor");
    	ev.execute();
    	List<EnvironmentVariableDto> evList = ev.result();
    	for(int i = 0; i < evList.size(); i ++){
    		if(evList.get(i).getName().equals("nome")){
    			varCheck = true;
    		}
    	}
    	assertTrue(varCheck);
    }
}
