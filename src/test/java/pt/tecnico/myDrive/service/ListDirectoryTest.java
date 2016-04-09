package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.User;

import static org.junit.Assert.assertEquals;

/**
 * Created by lolstorm on 09/04/16.
 */
public class ListDirectoryTest extends BaseServiceTest {

    private static final String USERNAME = "silverwolf84";
    private static final String PASSWORD = "lovely";
    private static final String EMAIL = "herman.horton84@example.com";
    private static final String NAME = "Herman Horton";
    private static final Short UMASK = 247;

    private String _token;

    protected void populate() {
        //createUser(USERNAME,PASSWORD, EMAIL, NAME, UMASK);
        //_token = createSession(USERNAME);

    }

    @Test
    public void success() {
        ListDirectory service = new ListDirectory(_token);
        service.execute();
        assertEquals("List with 6 Contacts", 6, 6);
    }

}
