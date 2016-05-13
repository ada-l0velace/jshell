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
    private File _app; private static final String APPNAME = "exe";
    private File _appExe;
    private static final String APP_CONTENT = "pt.tecnico.myDrive.presentation.Hello.execute";
    private static final String APP_EXE_CONTENT = "pt.tecnico.myDrive.presentation.Hello.greet";
	private static final String _appExtName = "App.exe";
	private static final String _path = "/home/toni/Documents";
	private static final String _content = "pt.tecnico.myDrive.presentation.Hello.execute";
	private User _user;

	protected void populate() { // populate mockup
		_user = createUser(_username, _pass, "Toni", (short) 0xF0);

    }

	
    @Test
    public void success(@Mocked Hello h) throws Exception {

        // Login Service

        LoginUserService login = new LoginUserService(_username, _pass);
        login.execute();
        String token = login.result();

        // Create File Service


        new CreateFileService(token, _nameDir1, FileType.DIRECTORY).execute();
        new CreateFileService(token, _nameDir2, FileType.DIRECTORY).execute();
        new CreateFileService(token, _nameApp, FileType.APP, APP_CONTENT).execute();
        new CreateFileService(token, _appExtName, FileType.APP, APP_EXE_CONTENT).execute();

        _appExe = _user.getFileByPath("/home/toni/" + _appExtName, token);
        _app = _user.getFileByPath("/home/toni/App", token);

        // List File Service

        ListDirectoryService ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 6); // ., .., Documents, Games, App, App.exe

        // Delete File Service

    	new DeleteFileService(token, _nameDir2).execute();
    	
    	ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 5); //., .., Documents, App, App.exe
    	
    	new WritePlainFileService(token, _nameApp, _content).execute();

        // Read PlainFile

        ReadPlainFileService rpf = new ReadPlainFileService(token, _nameApp);
    	rpf.execute();    	
        assertEquals(rpf.result(), _content); //"pt.tecnico.myDrive.presentation.Hello.execute"

        // Change Directory
        ChangeDirectoryService cd = new ChangeDirectoryService(token, _path);
        cd.execute();


        // List directory
        new CreateFileService(token, _nameDir2, FileType.DIRECTORY).execute();
        
        ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 3); // ., .., Games

        // Execute File app
    	ExecuteFileService service = new ExecuteFileService(token, _app.getPath() + _app.getName(), new String[]{});
        service.execute();
        new FullVerifications() { 
        	{
                h.execute(new String[]{});
                times = 1;
            }
        };

        // EnvVar Service

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

        // Execute File app with extension
        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                String [] args1 = { _appExe.getPath() + _appExe.getName() };
                _app.execute(token, args1);
            }
        };

        String path = _appExe.getPath() +_appExe.getName();
        String [] args1 = { _appExe.getPath() + _appExe.getName() };
        ExecuteFileService execService = new ExecuteFileService(token, path, args1);
        execService.execute();

        new FullVerifications() {
            {
                String [] args1 = { _appExe.getPath() + _appExe.getName() };
                h.greet(args1);
                times = 0;
                h.execute(args1);
                times = 1;
            }
        };
    }
}
