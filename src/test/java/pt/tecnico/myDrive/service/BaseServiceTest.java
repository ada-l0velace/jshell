package pt.tecnico.myDrive.service;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.myDrive.Main;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.InvalidUserCredentialsException;
import pt.tecnico.myDrive.service.factory.Factory;
import pt.tecnico.myDrive.service.factory.FileFactoryProducer;

public abstract class BaseServiceTest {
    protected static final Logger log = LogManager.getRootLogger();

    /**
     * Run once before each test class
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeAll() throws Exception {
        // run tests with a clean database!!!
        Main.init();
    }

    /**
     * Run before each test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        try {
            FenixFramework.getTransactionManager().begin(false);
            populate();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Rollback after each test
     */
    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * Each test adds its own data.
     */
    protected abstract void populate();

    /**
     * Creates a user in the application.
     * @param username (String) represents the username of the user.
     * @param password (String) represents the password of the user.
     * @param name (String) represents the name of the user.
     * @param umask (Short) represents the umask of the user.
     * @return User returns the user created.
     */
    User createUser(String username, String password, String name, Short umask) {
        return new User(name, username, password, umask, Manager.getInstance());
    }

    File createFile(Factory.FileType fileT, String token, String name) {
        return createFile(fileT, token, name, "");
    }

    File createFile(Factory.FileType fileT, String token, String name, String content) {
        Factory factory = FileFactoryProducer.getFactory(fileT, token);
        return factory.CreateFile(name, content);
    }

    /**
     * Creates a session for a specific username.
     * @param username (String) represents the username of the user.
     * @return token returns the token of the session created.
     */
    String createSession(String username, String password) {
        return Manager.getInstance().createSession(username, password);
    }

}