package pt.tecnico.myDrive.service;

import mockit.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import pt.tecnico.myDrive.Main;
import pt.tecnico.myDrive.exception.ExecFormatErrorException;
import pt.tecnico.myDrive.presentation.Hello;
import pt.tecnico.myDrive.service.factory.Factory.FileType;

import pt.tecnico.myDrive.domain.*;

import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.ExecutePermissionException;
import pt.tecnico.myDrive.exception.CannotBeExecutedException;

import java.util.Formatter;

import static org.junit.Assert.fail;

@RunWith(JMockit.class)
public class ExecuteFileServiceTest extends TokenVerificationTest {
    private File _directory1;
    private File _directory2;
    private File _plainfile1;
    private File _plainfile2;
    private File _plainfile3;
    private File _linktoapp1;
    private File _linktoapp2;
    private File _linktoplainfile1;
    private File _linktoplainfile2;
    private File _linktodirectory1;
    private File _linktolinktoapp1;
    private File _linktolinktoapp2;
    private File _linktolinktoplain1;
    private File _linktolinktoplain2;
    private File _app1;
    private File _app2;

    private static final String _directoryName1 = "Documents1";
    private static final String _plainfileName1 = "PlainFile1";
    private static final String _plainfileName2 = "PlainFile2";
    private static final String _plainfileName3 = "PlainFile3";
    private static final String _linkName1 = "LinkToApp1";
    private static final String _linkName2 = "LinkToApp2";
    private static final String _linktoplainfile1Name = "LinkToPlainFile1";
    private static final String _linktoplainfile2Name = "LinkToPlainFile2";
    private static final String _linktodirectory1Name = "LinkToDirectory";
    private static final String _linktolinktoapp1Name = "LinkToLinkToApp1";
    private static final String _linktolinktoapp2Name = "LinkToLinkToApp2";
    private static final String _linktolinktoplain1Name = "LinkToLinkToPlain1";
    private static final String _linktolinktoplain2Name = "LinkToLinkToPlain2";
    private static final String _appName1 = "Application1";
    private static final String _appName2 = "Application2";
    private static final String _appContent1 = "pt.tecnico.myDrive.presentation.Hello.greet";
    private static final String _appContent2 = "pt.tecnico.myDrive.presentation.Hello";
    private static final String _name1 = "Name1";
    private static final String _name2 = "Name2";
    private static final String _name3 = "Name3";
    private static final String _username1 = "User1";
    private static final String _username2 = "User2";
    private static final String _username3 = "User3";
    private static final String _password1 = "password1";
    private static final String _password2 = "password2";
    private static final String _password3 = "password3";
    private static final Short _umask1 = 0xFD;
    private static final Short _umask2 = 0xDF;
    private static final Short _umask3 = 0xFF;
    private User _user1;
    private User _user2;
    private User _user3;
    private String _token1;
    private String _token2;
    private String _token3;
    private Session _session1;
    private Session _session2;
    private Session _session3;


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
    private File _appWithExtension; private static final String APP_WITH_NAME = "App.exe";
    private File _plainTxt; private static final String PLAIN_TXT_NAME = "txt";


    //Links and Link names.
    private File _linkDirectory; private static final String LINK_DIRECTORY = "DirLink.exe";
    private File _linkPlain; private static final String LINK_PLAIN = "PlainLink.exe";
    private File _linkApp; private static final String LINK_APP = "AppLink.exe";
    private File _linkLinkApp; private static final String LINK_LINK_APP = "AppLinkLink.exe";

    //Other Stuff for Files.
    private static final String APP_CONTENT = "pt.tecnico.myDrive.presentation.Hello.execute";
    private static final String APP_WITH_EXTENSION_CONTENT = "pt.tecnico.myDrive.presentation.Hello.greet";
    private static final String [] SUCESS_ARGS = {"tecnico-softeng", "es16tg_21-project"};

    protected void populate() {
        _user1 = createUser(_username1, _password1, _name1, _umask1);
        _user2 = createUser(_username2, _password2, _name2, _umask2);
        _user3 = createUser(_username3, _password3, _name3, _umask3);
        _token1 = createSession(_username1, _password1);
        _token2 = createSession(_username2, _password2);
        _token3 = createSession(_username3, _password3);
        _session1 = Manager.getInstance().getSessionByToken(_token1);
        _session2 = Manager.getInstance().getSessionByToken(_token2);
        _session3 = Manager.getInstance().getSessionByToken(_token3);

        _directory1 = createFile(FileType.DIRECTORY, _token1, _directoryName1);
        _app1 = createFile(FileType.APP, _token1, _appName1, _appContent1);
        _app2 = createFile(FileType.APP, _token2, _appName2, _appContent2);

        String _plainfileContent1 = _app1.getPath() + _app1.getName() + " " + "Biana";
        String _plainfileContent2 = _plainfileContent1 + "\n" + _app2.getPath() + _app2.getName();
        String _plainfileContent3 = _app2.getPath() + _app2.getName();
        _plainfile1 = createFile(FileType.PLAINFILE, _token1, _plainfileName1, _plainfileContent1);
        _plainfile2 = createFile(FileType.PLAINFILE, _token2, _plainfileName2, _plainfileContent2);
        _plainfile3 = createFile(FileType.PLAINFILE, _token3, _plainfileName3, _plainfileContent3);

        _linktoapp1 = createFile(FileType.LINK, _token1, _linkName1, _app1.getPath()+_app1.getName());
        _linktoapp2 = createFile(FileType.LINK, _token2, _linkName2, _app2.getPath()+_app2.getName());
        _linktoplainfile1 = createFile(FileType.LINK, _token1, _linktoplainfile1Name, _plainfile1.getPath() + _plainfile1.getName()); 
        _linktoplainfile2 = createFile(FileType.LINK, _token2, _linktoplainfile2Name, _plainfile2.getPath() + _plainfile2.getName()); 
        _linktodirectory1 = createFile(FileType.LINK, _token1, _linktodirectory1Name,  _directory1.getPath() + _directory1.getName()); 
        _linktolinktoapp1 = createFile(FileType.LINK, _token1, _linktolinktoapp1Name, _linktoapp1.getPath() + _linktoapp1.getName()); 
        _linktolinktoapp2 = createFile(FileType.LINK, _token2, _linktolinktoapp2Name, _linktoapp2.getPath() + _linktoapp2.getName());
        _linktolinktoplain1 = createFile(FileType.LINK, _token1, _linktolinktoplain2Name, _linktoplainfile1.getPath() + _linktoplainfile1.getName());
        _linktolinktoplain2 = createFile(FileType.LINK, _token2, _linktolinktoplain2Name, _linktoplainfile2.getPath() + _linktoplainfile2.getName());




        //File ASSOCIATION
        _user = createUser(USERNAME, PASSWORD, NAME, UMASK);
        _token = createSession(USERNAME, PASSWORD);

       _plainWithoutExtension = createFile(FileType.PLAINFILE, _token, PLAIN_WITHOUT_EXTENSION);

        _appWithExtension = createFile(FileType.APP, _token, APP_WITH_NAME, APP_WITH_EXTENSION_CONTENT);

        _plain = createFile(FileType.PLAINFILE, _token, PLAIN_NAME);
        _dir = createFile(FileType.DIRECTORY, _token, DIR_NAME);
        _app = createFile(FileType.APP, _token, APPNAME, APP_CONTENT);
        String plainTxtContent = _app.getPath() + _app.getName() + " " + SUCESS_ARGS[0] + " " + SUCESS_ARGS[1];
        _plainTxt = createFile(FileType.PLAINFILE, _token, PLAIN_TXT_NAME, plainTxtContent);

        _linkDirectory = createFile(FileType.LINK, _token, LINK_DIRECTORY, _dir.getPath() + _dir.getName());
        _linkPlain = createFile(FileType.LINK, _token, LINK_PLAIN, _plain.getPath() + _plain.getName());
        _linkApp = createFile(FileType.LINK, _token, LINK_APP, _app.getPath() + _app.getName());
        _linkLinkApp = createFile(FileType.LINK, _token, LINK_LINK_APP, _linkApp.getPath() + _linkApp
                .getName());
    }


    @Test(expected = FileNotFoundException.class)
    public void executeNonExistingApp() {
        ExecuteFileService service = new ExecuteFileService(_token1, _app1.getPath() + "null");
        service.execute();
    }

    @Test(expected = FileNotFoundException.class)
    public void executeNonExistingPlain() {
        ExecuteFileService service = new ExecuteFileService(_token1, _plainfile1.getPath() + "null");
        service.execute();
    }

    @Test(expected = CannotBeExecutedException.class)
    public void executeLinkToDirectory() {
        ExecuteFileService service = new ExecuteFileService(_token1, _linktodirectory1.getPath() + _linktodirectory1.getName());
		User u = Manager.getInstance().getUserByToken(_token1);
		File d = u.getFileByPath(_linktodirectory1.getPath() + _linktodirectory1.getName(), _token1);
		System.out.println("Link to directory is " + d.getPath() + d.getName() + " pointed to " + d.getContent(_token1) + " instead of " + _directory1.getPath() + _directory1.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeApp1ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _app1.getPath() + _app1.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeApp2ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _app2.getPath() + _app2.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLink1ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktoapp1.getPath() + _linktoapp1.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLink2ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktoapp2.getPath() + _linktoapp2.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLinkToLink1ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktolinktoapp1.getPath() + _linktolinktoapp1.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLinkToLink2ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktolinktoapp2.getPath() + _linktolinktoapp2.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executePlain1ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _plainfile1.getPath() + _plainfile1.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executePlain2ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _plainfile2.getPath() + _plainfile2.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLinkToPlain1ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktoplainfile1.getPath() + _linktoplainfile1.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLinkToPlain2ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktoplainfile2.getPath() + _linktoplainfile2.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLinkToLinkToPlain1ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktolinktoplain1.getPath() + _linktolinktoplain1.getName());
        service.execute();
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeLinkToLinkToPlain2ByUser2() {
        ExecuteFileService service = new ExecuteFileService(_token2, _linktolinktoplain2.getPath() + _linktolinktoplain2.getName());
        service.execute();
    }


// ----------------------------------------------------------------------------------------------------------------

    protected void executeFile(File f) {
        String path = f.getPath();
        String [] args = { path + f.getName() };
        ExecuteFileService service = new ExecuteFileService(_token, path, args);
        service.execute();
    }

    @Test
    public void executePlainFile1(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _plainfile1.getPath() + _plainfile1.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;
            }
        };
    }

    @Test
    public void executePlainFile2(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _plainfile2.getPath() + _plainfile2.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;

                String [] arg = {  };
                h.main(arg);
                times = 1;
            }
        };
    }

    @Test
    public void executePlainFile3(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _plainfile3.getPath() + _plainfile3.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = {  };
                h.main(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeApp1(@Mocked Hello h) {
        String [] args = { "Biana" };
        ExecuteFileService service = new ExecuteFileService(_token1, _app1.getPath() + _app1.getName(), args);
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeApp2(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _app2.getPath() + _app2.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = {  };
                h.main(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeLinkToApp1(@Mocked Hello h) {
        String [] args = { "Biana" };
        ExecuteFileService service = new ExecuteFileService(_token1, _linktoapp1.getPath() + _linktoapp1.getName(), args);
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeLinkToApp2(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _linktoapp2.getPath() + _linktoapp2.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = {  };
                h.main(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeLinkToLinkToApp1(@Mocked Hello h) {
        String [] args = { "Biana" };
        ExecuteFileService service = new ExecuteFileService(_token1, _linktolinktoplain1.getPath() + _linktolinktoplain1.getName(), args);
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeLinkToLinkToApp2(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _linktolinktoplain2.getPath() + _linktolinktoplain2.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = {  };
                h.main(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeLinkToPlainFile1(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _linktoplainfile1.getPath() + _linktoplainfile1.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeLinkToPlainFile2(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _linktoplainfile2.getPath() + _linktoplainfile2.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;

                String [] arg = {  };
                h.main(arg);
                times = 1;
            }
        };
    }


    @Test
    public void executeLinkToLinkToPlainFile1(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _linktolinktoplain1.getPath() + _linktolinktoplain1.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;
            }
        };
    }

    @Test
    public void executeLinkToLinkToPlainFile2(@Mocked Hello h) {
        ExecuteFileService service = new ExecuteFileService(_token1, _linktolinktoplain2.getPath() + _linktolinktoplain2.getName());
        service.execute();

        new FullVerifications() {
            {
                String [] args = { "Biana" };
                h.greet(args);
                times = 1;

                String [] arg = {  };
                h.main(arg);
                times = 1;
            }
        };
    }

    

    @Test
    public void successPlainFileAssociationExec(@Mocked Hello h) {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _app.execute(_token, null);
            }
        };

        executeFile(_plain);

        new Verifications() {
            {
                String [] args = {_linkPlain.getPath() + _linkPlain.getName()};
                h.execute(args);
                times = 1;
            }
        };
    }

    public void successLinkDirectoryAssociationExec(@Mocked Hello h) {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _app.execute(_token, null);
            }
        };

        executeFile(_linkDirectory);

        new Verifications() {
            {
                String [] args = {_linkPlain.getPath() + _linkPlain.getName()};
                h.execute(args);
                times = 1;
            }
        };
    }

    @Test
    public void successLinkPlainFileAssociationExec(@Mocked Hello h) {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _app.execute(_token, null);
            }
        };

        executeFile(_linkPlain);

        new Verifications() {
            {
                String [] args = {_linkPlain.getPath() + _linkPlain.getName()};
                h.execute(args);
                times = 1;
            }
        };
    }

    @Test
    public void successLinkAppAssociationExec(@Mocked Hello h) {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _app.execute(_token, null);
            }
        };

        executeFile(_linkApp);

        new Verifications() {
            {
                String [] args = {_linkApp.getPath() + _linkApp.getName()};
                h.execute(args);
                times = 1;
            }
        };
    }

    @Test
    public void successLinkLinkAppAssociationExec(@Mocked Hello h) {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _app.execute(_token, null);
            }
        };

        executeFile(_linkLinkApp);

        new Verifications() {
            {
                String [] args = {_linkLinkApp.getPath() + _linkLinkApp.getName()};
                h.execute(args);
                times = 1;
            }
        };
    }

    @Test
    public void successPlainTxtAssociationExec(@Mocked Hello h) {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _app.execute(_token, null);
            }
        };

        executeFile(_plainTxt);

        new FullVerifications() {
            {
                String [] args = {SUCESS_ARGS[0], SUCESS_ARGS[1]};
                h.execute(args);
                times = 1;
            }
        };
    }

    @Test
    public void successAppWithExtensionAssociationExec(@Mocked Hello h) {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() {
                _appWithExtension.execute(_token, null);
            }
        };

        executeFile(_appWithExtension);

        new FullVerifications() {
            {
                h.execute(new String[]{});
                times = 0;
                h.greet(new String[]{});
                times = 1;
            }
        };
    }

    @Test(expected = ExecFormatErrorException.class)
    public void DirectoryAssociationCanNotBeExecuted() {

        new MockUp<ExecuteFileService>() {
            @Mock
            void dispatch() throws ExecFormatErrorException {
                throw new ExecFormatErrorException(_linkDirectory.getName());
            }
        };

        executeFile(_linkDirectory);
    }

    @Override
    public MyDriveService CreateService(String token) {
        return new ExecuteFileService(_token, _app.getPath() + _app.getName(), SUCESS_ARGS);
    }
}
