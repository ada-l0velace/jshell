package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

import java.util.List; 

private User _user;
private Session _session;
private String _token;
private String _name;
private String _value;


public class EnvironmentVariableService extends LoginRequiredService {
    public EnvironmentVariableService(String token, String name, String value) {
        super(token);
        _token = token;
        _name = name;
        _value = value;
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
        
    }

    public List<EnvironmentVariableDto> result() {
        return null;
    }
}
