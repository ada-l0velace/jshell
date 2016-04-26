package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.domain.*;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.ReadPermissionException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;

public class ReadPlainFileService extends LoginRequiredService {
	
	private String _plainFileToken;
    private String _plainFileName;
    private String _content;
    
    public ReadPlainFileService(String token, String plainFileName) {
    	super(token);
        _plainFileToken = token;
    	_plainFileName = plainFileName;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_plainFileToken);
        Directory cd = s.getCurrentDirectory();
        File pf;
    	if((pf = cd.search(_plainFileName, _plainFileToken)) instanceof PlainFile){
    		_content = pf.getContent(_plainFileToken);
            return;
    	}
        throw new InvalidFileTypeException("Not a plainFile");

    }
    
    public String result() {
        return _content;
    }
//so para commit
}
