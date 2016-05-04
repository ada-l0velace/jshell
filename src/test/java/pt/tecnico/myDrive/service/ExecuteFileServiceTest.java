package pt.tecnico.myDrive.service;

import mockit.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import pt.tecnico.myDrive.Main;
import pt.tecnico.myDrive.exception.ExecFormatErrorException;
import pt.tecnico.myDrive.service.factory.Factory.FileType;

import pt.tecnico.myDrive.domain.*;

import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.ExecutePermissionException;
import pt.tecnico.myDrive.exception.CannotBeExecutedException;

@RunWith(JMockit.class)
public class ExecuteFileServiceTest extends TokenVerificationTest {
    private File _directory1;
    private File _directory2;
    private File _plainfile1;
    private File _link1;
    private File _link2;
    private File _app1;
    private File _app2;

    private static final String _directoryName1 = "Documents1";
    private static final String _directoryName2 = "Documents2";
    private static final String _plainfileName1 = "PlainFile1";
    private static final String _linkName1 = "Link1";
    private static final String _linkName2 = "Link2";
    private static final String _appName1 = "Application1";
    private static final String _appName2 = "Application2";
    private static final String _appContent1 = "pt.tecnico.myDrive.Main";
    private static final String _appContent2 = "pt.tecnico.myDrive.Main";
    private static final String _plainfileContent1 = "Nothing really matters!";
    private static final String _name1 = "Name1";
    private static final String _name2 = "Name2";
    private static final String _username1 = "User1";
    private static final String _username2 = "User2";
    private static final String _password1 = "password1";
    private static final String _password2 = "password2";
    private static final Short _umask1 = 0xFB;
    private static final Short _umask2 = 0xBF;
    private User _user1;
    private User _user2;
    private String _token1;
    private String _token2;
    private Session _session1;
    private Session _session2;


    // FILE ASSOCIATION DATA

    //User Info
    private User _user;
    private String _token;
    private static final String USERNAME = "David";
    private static final String PASSWORD = "Mediocre";
    private static final String NAME = "David Matos";
    private static final Short UMASK = 0xF0;

    //Files and File names.
    private File _dir; private static final String DIR_NAME = "Dir.exe";
    private File _plain; private static final String PLAIN_NAME = "Plain.exe";
    private File _plainWithoutExtension; private static final String PLAIN_WITHOUT_EXTENSION = "ScrubMaster";
    private File _app; private static final String APPNAME = "exe";

    //Links and Link names.
    private File _linkDirectory; private static final String LINK_DIRECTORY = "DirLink.exe";
    private File _linkPlain; private static final String LINK_PLAIN = "PlainLink.exe";
    private File _linkApp; private static final String LINK_APP = "AppLink.exe";
    private File _linkLinkApp; private static final String LINK_LINK_APP = "AppLinkLink.exe";

    //Other Stuff for Files.
    private static final String APP_CONTENT = "pt.tecnico.myDrive.domain.Main.appExec";
    private static final String [] SUCESS_ARGS = {"tecnico-softeng", "es16tg_21-project"};

    protected void populate() {
        _user1 = createUser(_username1, _password1, _name1, _umask1);
        _user2 = createUser(_username2, _password2, _name2, _umask2);
        _token1 = createSession(_username1, _password1);
        _token2 = createSession(_username2, _password2);
        _session1 = Manager.getInstance().getSessionByToken(_token1);
        _session2 = Manager.getInstance().getSessionByToken(_token2);

        _plainfile1 = createFile(FileType.PLAINFILE, _token1, _plainfileName1, _plainfileContent1);
        _directory1 = createFile(FileType.DIRECTORY, _token1, _directoryName1);
        _directory2 = createFile(FileType.DIRECTORY, _token2, _directoryName2);
        _app1 = createFile(FileType.APP, _token1, _appName1, _appContent1);
        _app2 = createFile(FileType.APP, _token2, _appName2, _appContent2);
        _link1 = createFile(FileType.LINK, _token1, _linkName1, _app1.getPath()+_app1.getName());
        _link2 = createFile(FileType.LINK, _token2, _linkName2, _app2.getPath()+_app2.getName());

        //File ASSOCIATION
        _user = createUser(USERNAME, PASSWORD, NAME, UMASK);
        _token = createSession(USERNAME, PASSWORD);

        _plainWithoutExtension = createFile(FileType.PLAINFILE, _token, PLAIN_WITHOUT_EXTENSION);
        _plain = createFile(FileType.PLAINFILE, _token, PLAIN_NAME);
        _dir = createFile(FileType.DIRECTORY, _token, DIR_NAME);
        _app = createFile(FileType.APP, _token, APPNAME, APP_CONTENT);

        _linkDirectory = createFile(FileType.LINK, _token, LINK_DIRECTORY, _dir.getPath() + _dir.getName());
        _linkPlain = createFile(FileType.LINK, _token, LINK_PLAIN, _plain.getPath() + _plain.getName());
        _linkApp = createFile(FileType.LINK, _token, LINK_APP, _app.getPath() + _app.getName());
        _linkLinkApp = createFile(FileType.LINK, _token, LINK_LINK_APP, _linkApp.getPath() + _linkApp
                .getName());
    }

    @Test
    public void success() {

    }

    @Test(expected = FileNotFoundException.class)
    public void executeNonExistingApp() {
        ExecuteFileService service = new ExecuteFileService(_token1, _app1.getPath() + "null", null);
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeUserNoGlobalAppPermission() {
        ExecuteFileService service = new ExecuteFileService(_token1, _app2.getPath(), null);
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeUserNoLocalAppPermission() {
        ExecuteFileService service = new ExecuteFileService(_token2, _app2.getPath(), null);
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeAppNoGlobalPermission() {
        ExecuteFileService service = new ExecuteFileService(_token2, _app1.getPath(), null);
        service.execute();
    }

    @Test(expected = FileNotFoundException.class)
    public void executeNonExistingLink() {
        ExecuteFileService service = new ExecuteFileService(_token1, _link1.getPath() + "null", null);
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeUserNoGlobalLinkPermission() {
        ExecuteFileService service = new ExecuteFileService(_token1, _link2.getPath(), null);
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeUserNoLocalLinkPermission() {
        ExecuteFileService service = new ExecuteFileService(_token2, _link2.getPath(), null);
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLinkNoGlobalPermission() {
        ExecuteFileService service = new ExecuteFileService(_token2, _link1.getPath(), null);
        service.execute();
    }

    @Test(expected = CannotBeExecutedException.class)
    public void executeDirectory() {
        ExecuteFileService service = new ExecuteFileService(_token1, _directory1.getPath(), null);
        service.execute();
    }

    @Test(expected = CannotBeExecutedException.class)
    public void executePlainFile() {
        ExecuteFileService service = new ExecuteFileService(_token1, _plainfile1.getPath(), null);
        service.execute();
    }


    protected void executeFile(File f) {
        String path = f.getPath();
        String [] args = { path + f.getName() };
        ExecuteFileService service = new ExecuteFileService(_token, path, args);
        service.execute();
    }

    @Test
    public void successPlainFileAssociationExec() {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _plain.execute(_token);
            }
        };

        executeFile(_plain);

        new FullVerifications() {
            {
                String [] args = {_linkPlain.getPath() + _linkPlain.getName()};
                Main.appTest(args);
                times = 1;
            }
        };
    }

    public void successLinkDirectoryAssociationExec() {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _linkDirectory.execute(_token);
            }
        };

        executeFile(_linkDirectory);

        new FullVerifications() {
            {
                String [] args = {_linkPlain.getPath() + _linkPlain.getName()};
                Main.appTest(args);
                times = 1;
            }
        };
    }

    @Test
    public void successLinkPlainFileAssociationExec() {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _linkPlain.execute(_token);
            }
        };

        executeFile(_linkPlain);

        new FullVerifications() {
            {
                String [] args = {_linkPlain.getPath() + _linkPlain.getName()};
                Main.appTest(args);
                times = 1;
            }
        };
    }

    @Test
    public void successLinkAppAssociationExec() {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _linkApp.execute(_token);
            }
        };

        executeFile(_linkApp);

        new FullVerifications() {
            {
                String [] args = {_linkApp.getPath() + _linkApp.getName()};
                Main.appTest(args);
                times = 1;
            }
        };
    }

    @Test
    public void successLinkLinkAppAssociationExec() {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _linkLinkApp.execute(_token);
            }
        };

        executeFile(_linkLinkApp);

        new FullVerifications() {
            {
                String [] args = {_linkLinkApp.getPath() + _linkLinkApp.getName()};
                Main.appTest(args);
                times = 1;
            }
        };
    }

    @Test(expected = ExecFormatErrorException.class)
    public void PlainFileDoesNotHaveExtension() {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() throws ExecFormatErrorException {
                throw new ExecFormatErrorException(_plainWithoutExtension.getName());
            }
        };

        executeFile(_plainWithoutExtension);

    }



    @Override
    public MyDriveService CreateService(String token) {
        return new ExecuteFileService(_token, _app.getPath() + _app.getName(), SUCESS_ARGS);
    }
}