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
public class LoginUserService extends MyDriveService {

    private String _userToken;
    private String _username;
    private String _password;

    public LoginUserService(String username) {
        this(username,"");
    }

    public LoginUserService(String username, String password) {
        super();
        _username = username;
        _password = password;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        Session s = new Session(_username, _password);
        _userToken = s.getToken();
        User u = Manager.getInstance().getUserByToken(_userToken);
        u.removeExpiredSessions();
    }

    public String result() {
        return _userToken;
    }
}
