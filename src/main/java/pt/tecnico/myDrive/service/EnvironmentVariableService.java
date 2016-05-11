package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

import pt.tecnico.myDrive.exception.InvalidVariableNameException;;
import pt.tecnico.myDrive.exception.EmptyVariableValueException;

import java.util.List; 

public class EnvironmentVariableService extends LoginRequiredService {

    private User _user;
    private Session _session;
    private String _token;
    private String _name;
    private String _value;
    private List<EnvironmentVariableDto> _list;

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
        if(_name.equals(null) || _name.equals("") )
        {
            throw new InvalidVariableNameException(_name);
        }
        if(_value.equals(null) || _value.equals("") )
        {
            throw new InvalidVariableNameException(_name);
        }

        boolean found = false;

        for(EnvironmentVariable envVar : _session.getEnvVarSet())
        {
            if(envVar.getName().equals(_name))
            {
                envVar.setValue(_value);
                found = true;
                break;
            }
        }
        if(!found)
        {
            EnvironmentVariable newEnvVar = new EnvironmentVariable();
            newEnvVar.setName(_name);
            newEnvVar.setValue(_value);
            _session.addEnvVar(newEnvVar);
        }

    }

    public List<EnvironmentVariableDto> result() {
        _list = new ArrayList<>();
        for(EnvironmentVariable envVar : _session.getEnvVarSet()) 
        {
            EnvironmentVariableDto newEnvVarDto = new EnvironmentVariableDto(envVar.getName(),envVar.getValue());
            _list.add(newEnvVarDto);
        }
        return _list;
    }
}
