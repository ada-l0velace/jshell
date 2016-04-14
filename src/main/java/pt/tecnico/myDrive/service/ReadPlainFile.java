package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.PlainFile;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;

public class ReadPlainFile extends LoginRequiredService {
	
	private String _plainFileToken;
    private String _plainFileName;
    private String _content;
    
    public ReadPlainFile(String token, String plainFileName) {
    	super(token);
        _plainFileToken = token;
    	_plainFileName = plainFileName;
    }

    @Override
    protected void dispatch() throws MyDriveException {
    	super.dispatch();
    	if(Manager.getInstance().getSessionByToken(_plainFileToken).getCurrentDirectory().getFileByPath(_plainFileName) instanceof PlainFile){
    		PlainFile pf = (PlainFile)Manager.getInstance().getSessionByToken(_plainFileToken).getCurrentDirectory().getFileByPath(_plainFileName);
            _content = pf.getContent(Manager.getInstance().getUserByToken(_plainFileToken));
    	}
        throw new InvalidFileTypeException("not a plainFile");

    }
    
    public String result() {
        return _content;
    }
//so para commit
}
