package pt.tecnico.myDrive.domain;

public class EnvironmentVariable extends EnvironmentVariable_Base {
    
    public EnvironmentVariable() {
        super();
    }

    protected void remove()
    {
        setSession(null);
        deleteDomainObject();
    }
    
}
