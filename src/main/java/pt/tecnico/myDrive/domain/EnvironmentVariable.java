package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.EnvironmentVariableAlreadyExists;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

public class EnvironmentVariable extends EnvironmentVariable_Base {
    
    public EnvironmentVariable() {
        super();
    }

    public EnvironmentVariable(String name, String value, Session s) throws EnvironmentVariableAlreadyExists {
        this();
        if (s.getEnvVarByName(name) != null) {
            throw new EnvironmentVariableAlreadyExists(name);
        }
        setName(name);
        setValue(value);
        setSession(s);
    }

    public void modify (String name, String value) {
        setName(name);
        setValue(value);
    }

    @Override
    public void setSession(Session s) {
        if (s == null)
            remove();
        else {
            s.addEnvVar(this);
        }
    }

    public EnvironmentVariableDto toDto() {
        return new EnvironmentVariableDto(getName(), getValue());
    }

    /**
     * Removes Environment Variable from the domain.
     */
    protected void remove()
    {
        //setSession(null);
        super.setSession(null);
        deleteDomainObject();
    }
    
}
