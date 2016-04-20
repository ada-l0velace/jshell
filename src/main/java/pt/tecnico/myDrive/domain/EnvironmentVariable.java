package pt.tecnico.myDrive.domain;

public class EnvironmentVariable extends EnvironmentVariable_Base {
    
    public EnvironmentVariable() {
        super();
    }

    /**
     * Removes Environment Variable from the domain.
     */
    protected void remove()
    {
        setSession(null);
        deleteDomainObject();
    }
    
}
