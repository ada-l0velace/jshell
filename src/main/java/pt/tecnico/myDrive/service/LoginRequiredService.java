package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.UserNotInSessionException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;

/**
 * Created by lolstorm on 10/04/16.
 */
public class LoginRequiredService extends MyDriveService {
    private String _token;

    protected LoginRequiredService(String token) {
        _token = token;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        Session s = Manager.getInstance().getSessionByToken(_token);
        if(s == null) {
            throw new UserNotInSessionException(_token);
        }
        String username = Manager.getInstance().getUserByToken(_token).getUsername();
        if (s.hasExpired()) {
            throw new UserSessionExpiredException(username);
        }
        s.setLastActive(new DateTime(DateTimeZone.UTC));
    }
}
