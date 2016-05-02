package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.EnvironmentVariable;
import java.util.ArrayList;

import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;



public class AddEnvironmentVariableService extends LoginRequiredService {

    
    public AddEnvironmentVariableService(String token, String name){
        super(token);
    }

    @Override
    protected void dispatch() throws FileNotFoundException, UserSessionExpiredException, InvalidFileTypeException{
        super.dispatch();
    }

    public ArrayList<EnvironmentVariable> result(){
        return new ArrayList<EnvironmentVariable>();
    }
}
