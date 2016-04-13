package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;

import static org.junit.Assert.assertFalse;


public class ReadPlainFileTest extends TokenVerificationTest {

	private PlainFile _file;
	private static final String _fileName = "Testdoc";
	private static final String _fileType = "PlainFile";
	private static final String _username = "Jack";
	private static final String _testContent = "vanilla";
	private static final String CONTENT [] = {
	        "jack",
	        "3rdPlace",
	        "b0nj0vi",
	        "3lv1s",
	        "roger",
	        "AceOfSpades",
	        "caneloni",
	        "Garfield",
	        "",
	        "one\ntwo",
	        "test\ntest,test\ntest,test,test",
	        "Roses are Red \nMy name is dave \nThis makes no sence\n Microwave",
	        "\n\n\n\nX Marks the spot!!!",
	    };
	private static final String _password = "pwjack";
    private static final String _name = "Stevie";
    private static final Short _umask = 0xF0;
    private static final Short _umask0 = 0x00;
	private User _user;
	private String _token;
	private String _rootToken;
	private String _nonPremitionsToken;

	
	protected void populate() {
		for (String content : CONTENT ){
		_user = createUser(_username, _password, _name, _umask);
		_token = createSession(_username);
		_file = new PlainFile(_user, _fileName, content ,Manager.getInstance().getSessionByToken(_token).getCurrentDirectory(),
							Manager.getInstance());
		}
		//Creates user without premissions
		_user = createUser("derp", _password, _name, _umask0);
		_token = createSession("derp");
		_file = new PlainFile(_user, _fileName, _testContent ,Manager.getInstance().getSessionByToken(_token).getCurrentDirectory(),
							Manager.getInstance());
		
		//Creates SuperUser
		_user = createUser("root", _password, _name, _umask0);
		_rootToken = createSession("root");
		_file = new PlainFile(_user, _fileName,_testContent ,Manager.getInstance().getSessionByToken(_rootToken).getCurrentDirectory(),
				Manager.getInstance());
    }

    @Test
    public void success() {
        ReadPlainFile service = new ReadPlainFile(_token, _fileName);
        service.execute();

        // check file was readed
        assertFalse("File was not readed", _user.hasFile(_fileName));
    }
	
    @Test(expected = FileNotFoundException.class)
    public void readNonExistingFile() {
        ReadPlainFile service = new ReadPlainFile(_token, "Bin");
        service.execute();
    }
    
    @Override
    public MyDriveService CreateService(String token) {
        return new ReadPlainFile(token, _fileName);
    }
    
    @Test
    public void rootPlainFile() {
        ReadPlainFile service = new ReadPlainFile(_rootToken, _fileName);
        service.execute();

        // check file was readed
        assertFalse("File was not readed", _user.hasFile(_fileName));
    }
    
    @Test(expected = ReadPermissionException.class)
    public void readNonAcesivelFile() {
        ReadPlainFile service = new ReadPlainFile(_token, "Bin");
        service.execute();
    }
    
}
