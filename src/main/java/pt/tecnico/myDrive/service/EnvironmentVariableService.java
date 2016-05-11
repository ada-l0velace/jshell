package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

import java.util.ArrayList;
import java.util.List;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

import pt.tecnico.myDrive.exception.EnvVarNameNotFoundException; 

public class EnvironmentVariableService extends LoginRequiredService {

    private Session _session;
    private String _token;
    private String _name = "";
    private String _value = "";
    private List<EnvironmentVariableDto> _list;

    public EnvironmentVariableService(String token) {
        super(token);
        _token = token;
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
        
        _session = Manager.getInstance().getSessionByToken(_token);
        
        if(_value.equals(""))
        {	
        	if(_name.equals(""))
        	{
                for(EnvironmentVariable envVar : _session.getEnvVarSet())
                {
                    System.out.println(envVar.getName() + "=" + envVar.getValue());
                }
        	}
        	else
        	{
        		boolean found = false;
        		for(EnvironmentVariable envVar : _session.getEnvVarSet())
                {
                    if(envVar.getName().equals(_name))
                    {
                        System.out.println(envVar.getValue());
                        found = true;
                    }
                }
        		if(found == false)
        			throw new EnvVarNameNotFoundException(_name);
        	}
        }
        else
        {
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
