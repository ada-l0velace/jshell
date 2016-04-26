package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;


public class EnvironmentVariableService extends LoginRequiredService {
    public EnvironmentVariableService(String token) {
        super(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
    }
}
