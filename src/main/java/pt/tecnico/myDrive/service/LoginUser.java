package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;

import pt.tecnico.myDrive.exception.InvalidUserCredentialsException;
import pt.tecnico.myDrive.exception.MyDriveException;

/**
 * Created by lolstorm on 08/04/16.
 */
public class LoginUser extends MyDriveService {

    private String _userToken;
    private String _username;
    private String _password;
    public static String ROOT_USERNAME = SuperUser.ROOT_USERNAME;

    public LoginUser(String username, String password) {
        super();
        _username = username;
        _password = password;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        User u = Manager.getInstance().getUserByUsername(_username);
        if (u == null)
            throw new InvalidUserCredentialsException();
        if (!u.isValidPassword(_password))
            throw new InvalidUserCredentialsException();
        Session s = new Session(u);
        _userToken = s.getToken();
        s.remove();
    }

    public String result() {
        return _userToken;
    }
}
