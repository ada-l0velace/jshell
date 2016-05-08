package pt.tecnico.myDrive.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;

import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.*;
import pt.tecnico.myDrive.exception.*;
import pt.tecnico.myDrive.service.factory.Factory.FileType;


//@RunWith(JMockit.class)
public class IntegrationTest extends BaseServiceTest {

	private static final String _username = "toni", _pass = "tonitoni16";
	private static final String _nameDir1 = "Documents",  _nameDir2= "Games", _nameApp = "App";
	private static final String _path = "/home/toni/Documents";
	private static final String _content = "newContent";
	
	protected void populate() { // populate mockup
		createUser(_username, _pass, "Toni", (short) 0xF0);
    }

	
    @Test
    public void success() throws Exception {

    	LoginUserService login = new LoginUserService(_username, _pass);
        login.execute();
        String token = login.result();
        
        new CreateFileService(token, _nameDir1, FileType.DIRECTORY).execute();
        new CreateFileService(token, _nameDir2, FileType.DIRECTORY).execute();
        new CreateFileService(token, _nameApp, FileType.APP, "").execute();

        ListDirectoryService ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 5); // Documents, Games, App
    	
    	new DeleteFileService(token, _nameDir2).execute();
    	
    	ld = new ListDirectoryService(token);
    	ld.execute();
    	assertEquals(ld.result().size(), 4); //Documents, App
    	
    	new WritePlainFileService(token, _nameApp, _content).execute();
    	
    	ReadPlainFileService rpf = new ReadPlainFileService(token, _nameApp);
    	rpf.execute();    	
        assertEquals(rpf.result(), _content); //"newContent"
        
        ChangeDirectoryService cd = new ChangeDirectoryService(token, _path);
        cd.execute();
        
        new CreateFileService(cd.result(), _nameDir2, FileType.DIRECTORY).execute();
        
        ld = new ListDirectoryService(cd.result());
    	ld.execute();
    	assertEquals(ld.result().size(), 3); // Games
    	
    }
}
