package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.SuperUser;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.File;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.ReadPermissionException;

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
        File pf = Manager.getInstance().getSessionByToken(_plainFileToken).getCurrentDirectory().getFileByPath(_plainFileName, _plainFileToken);
        _content = pf.getContent(Manager.getInstance().getUserByToken(_plainFileToken));

    }
    
    public String result() {
        return _content;
    }

}
