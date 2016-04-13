package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
    private static final Short _umask0 = 0xF0;
	private User _user;
	private String _token;

    private User _user1;
    private String _token1;

    private PlainFile _file2;

    private String _rootToken;
	private String _nonPremitionsToken;

	
	protected void populate() {
		int i = 0;
        Manager m = Manager.getInstance();
		/*for (String content : CONTENT ){
			_user = createUser(_username+ i++, _password, _name, _umask);
			_token = createSession(_username + (i-1));
			_file = new PlainFile(_user, _fileName + i++, content ,Manager.getInstance().getSessionByToken(_token).getCurrentDirectory(),
								Manager.getInstance());
		}*/

		//Creates user without permissions
		_user = createUser("derp", _password, _name, _umask);
		_token = createSession("derp");
        //Second user the read.
        _user1 = createUser("derp1", _password, _name, _umask);
        _token1 = createSession("derp1");
        //Root token.
        _rootToken = createSession("root");
        //Change directory to check read permissions
        Session s = m.getSessionByToken(_token);
        new PlainFile(m.getSuperuser(), _fileName, _testContent , s.getCurrentDirectory(), m);
        new PlainFile(_user, _fileName + "1", _testContent , s.getCurrentDirectory(), m);


    }

    @Test
    public void success() {
        ReadPlainFile service = new ReadPlainFile(_token, _fileName);
        service.execute();
        assertEquals("Content is not returned", service.result(), _testContent);
    }

    @Test(expected = FileNotFoundException.class)
    public void PlainFileNotFound() {
        ReadPlainFile service = new ReadPlainFile(_token, "Bin");
        service.execute();

    }

    @Test
    public void readPublicPlainFile() {
        ReadPlainFile service = new ReadPlainFile(_token, _fileName);
        service.execute();
    }

    @Test(expected = ReadPermissionException.class)
    public void fileReadAccessDenied() {
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_token);
        Session s2 = m.getSessionByToken(_token1);
        // change user current directory.
        s2.setCurrentDirectory(s.getCurrentDirectory());

        ReadPlainFile service = new ReadPlainFile(_token1, _fileName);
        service.execute();
    }


    @Override
    public MyDriveService CreateService(String token) {
        return new ReadPlainFile(token, _fileName);
    }
    
}
