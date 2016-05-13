package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

import java.util.ArrayList;
import java.util.List;
import pt.tecnico.myDrive.exception.MyDriveException;

public class EnvironmentVariableService extends LoginRequiredService {

    private Session _session;
    private String _token;
    private String _name;
    private String _value;
    private List<EnvironmentVariableDto> _list;

    public EnvironmentVariableService(String token) {
        super(token);
        _token = token;
        _list = new ArrayList<EnvironmentVariableDto>();
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
        if(_name == null && _value == null) {
            _list = _session.EnvironmentVariablesToDto();
            return;
        }
        if(_name != null && _value == null) {
            _list.add(_session.getEnvVarByNameException(_name).toDto());
            return;
        }
        EnvironmentVariable ev = _session.getEnvVarByName(_name);

        if(ev == null)
            new EnvironmentVariable(_name, _value, _session);
        else
            ev.modify(_name, _value);

        _list = _session.EnvironmentVariablesToDto();
    }

    public String output() {
        if(_name == null && _value == null)
            return result().toString().replaceAll("[\\[\\]]", "")+ "\n";
        else if(_name != null && _value == null)
            return result().get(0).getValue()+ "\n";
        else
            return "";
    }

    public List<EnvironmentVariableDto> result() {
        return _list;
    }
}
