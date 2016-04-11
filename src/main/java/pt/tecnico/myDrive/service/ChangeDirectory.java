package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class ChangeDirectory extends MyDriveService {

    private String _sessionToken;
    
    public ChangeDirectory(Long token, String path){
        
    }

    @Override
    protected void dispatch() throws MyDriveException{}

    public String result(){
        return _sessionToken;
    }
}
