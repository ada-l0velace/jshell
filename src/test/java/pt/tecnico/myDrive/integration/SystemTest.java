package pt.tecnico.myDrive.integration;

import org.junit.Test;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.presentation.*;
import pt.tecnico.myDrive.service.factory.Factory.FileType;

public class SystemTest extends BaseServiceTest {

    private MyDriveShell  sh;
    private static final String APP_NAME = "exe";
    private static final String APP_CONTENT = "pt.tecnico.myDrive.presentation.Hello.greet";

    private static final String PLAIN_NAME = "Plain";
    private static final String PLAIN_ARGS = "hello darkness my old friend I've come to talk with you again.\n";
    private static final String PLAIN_CONTENT = "pt.tecnico.myDrive.presentation.Hello.execute "+ PLAIN_ARGS;

    private static final String ENV_VAR_NAME = "JAVA_HOME";
    private static final String ENV_VAR_VALUE = "/usr/bin/java/jdk1.8.0.0_25";
    private static final String ENV_VAR_NAME_MODIFIED = "JDK_HOME";
    private static final String ENV_VAR_VALUE_MODIFIED = "%JAVA_HOME%";

    protected void populate() {
        sh = new MyDriveShell();

        createFile(FileType.APP, createSession(SuperUser.ROOT_USERNAME, SuperUser.ROOT_PASSWORD), APP_NAME, APP_CONTENT);
        createFile(FileType.PLAINFILE, createSession(SuperUser.ROOT_USERNAME, SuperUser.ROOT_PASSWORD), PLAIN_NAME,
                APP_CONTENT);
    }

    @Test
    public void success() {
        new LoginUser(sh).execute(new String[] { SuperUser.ROOT_USERNAME, SuperUser.ROOT_PASSWORD } );
        new Token(sh).execute(new String[] { SuperUser.ROOT_USERNAME } );
        new ChangeDirectory(sh).execute(new String[] { "." } );
        new ListDirectory(sh).execute(new String[] {} );
        new ExecuteFile(sh).execute(new String[] { APP_NAME, "Hey"} );
        new WriteFile(sh).execute(new String[] { PLAIN_NAME, PLAIN_CONTENT } );
        new ExecuteFile(sh).execute(new String[] { PLAIN_NAME} );
        new EnvVariable(sh).execute(new String[] { ENV_VAR_NAME, ENV_VAR_VALUE} );
        new EnvVariable(sh).execute(new String[] { ENV_VAR_NAME, ENV_VAR_NAME_MODIFIED} );
        new EnvVariable(sh).execute(new String[] { ENV_VAR_VALUE_MODIFIED, ENV_VAR_NAME_MODIFIED} );
    }
}
