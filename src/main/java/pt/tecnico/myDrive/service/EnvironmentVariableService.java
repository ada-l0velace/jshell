package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.EnvironmentVariableDto;

import java.util.List;


public class EnvironmentVariableService extends LoginRequiredService {
    public EnvironmentVariableService(String token, String name, String value) {
        super(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
    }

    public List<EnvironmentVariableDto> result() {
        return null;
    }
}
