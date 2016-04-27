package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

/**
 * Created by lolstorm on 26/04/16.
 */
public class ExecuteFileService extends LoginRequiredService {
    public ExecuteFileService(String token, String path, String [] args) {
        super(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
    }
}
