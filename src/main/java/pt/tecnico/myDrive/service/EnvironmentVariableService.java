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
import pt.tecnico.myDrive.exception.EnvVarNameNotFoundException;

import java.util.List; 

public class EnvironmentVariableService extends LoginRequiredService {

    private User _user;
    private Session _session;
    private String _token;
    private String _name;
    private String _value;
    private List<EnvironmentVariableDto> _list;

    public EnvironmentVariableService(String token) {
        super(token);
        _token = token;
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
    }

    public EnvironmentVariableService(String token, String name) {
        this(token);
        _name = name;
    }

    public EnvironmentVariableService(String token, String name, String value) {
        this(token);
        _name = name;
        _value = value;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();

        boolean found = false;

        for(EnvironmentVariable envVar : _session.getEnvVarSet())
        {
            if(envVar.getName().equals(_name))
            {
                envVar.setValue(_value);
                found = true;
                System.out.println(envVar.getName());
                System.out.println(envVar.getValue());
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

    public String output(){
        String output = "";

        for(EnvironmentVariable envVar : _session.getEnvVarSet())
        {
            if(envVar.getName().equals(_name))
            {
                output = envVar.getName() + "=" + envVar.getValue();
                return output;
            }
        }
        throw new EnvVarNameNotFoundException(_name);
    }

    public String outputAll(){
        String output = "";

        for(EnvironmentVariable envVar : _session.getEnvVarSet())
        {
            output = output + envVar.getName() + "=" + envVar.getValue() + "\n";
        }
        return output;
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
