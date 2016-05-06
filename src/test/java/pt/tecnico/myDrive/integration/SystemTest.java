package pt.tecnico.myDrive.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.myDrive.service.BaseServiceTest ;
import pt.tecnico.myDrive.presentation.*;

public class SystemTest extends BaseServiceTest {

    private MyDriveShell  sh;
    /*
    private User _user;
    private String _token;
    private Link _pathLink;
    private App _file;
    private Manager _m;
    private PlainFile _plainfile;
    private Directory _directoria;
    private String _content = "abc";
    private String _fileName = "plainfile";
*/
    protected void populate() {
        sh = new MyDriveShell();
        /*
        _m = Manager.getInstance();
        _user = createUser("derp", _password, _name, _umask);
        _token = createSession("derp", _password);
        _plainfile = new PlainFile(_user, _fileName + "1", _testContent , s.getCurrentDirectory(), m);
        _directoria = new Directory(_user, "DirToTheFuture", s.getCurrentDirectory(), m);
        _file = new App(m.getSuperuser(), "AppToThePast", _testContent , s.getCurrentDirectory(), m);    
        _pathLink = _user.createLink(_file, "LinkToThePast", s.getCurrentDirectory(),m);
        new PlainFile(_user, _fileName+"1", _content , s.getCurrentDirectory(), m);
        */
    }

    @Test
    public void success() {
        /*
        new LoginUserService(sh).execute(_token);
        new ChangeDirectoryService(sh).execute(_token, _pathLink );
        new ListDirectoryService(sh).execute(_token );
        new ExecuteFileService(sh).execute(_token, _pathLink );
        new WritePlainFileService(sh).execute( _token, _plainfile, _content);
        new EnvironmentVariableService(sh).execute(_token, "derp", "test" );
        //new Token(sh).execute("derp");

        //ou
        
        new LoginUserService(sh).execute(new String[] { "login", "jack","secretpass" } );
        new ChangeDirectoryService(sh).execute(new String[] { "cwd", "./" } );
        new ListDirectoryService(sh).execute(new String[] { "ls"} ); //verificar a path
        new ExecuteFileService(sh).execute(new String[] { "do", "./", "arg" } ); //verificar a path e agumentos
        new WritePlainFileService(sh).execute(new String[] { "update", ".t", "abc" } );
        new EnvironmentVariableService(sh).execute(new String[] { "env" } );
        //new Token(sh).execute(new String[] {"token", "derp" } );
        */

    }
}
