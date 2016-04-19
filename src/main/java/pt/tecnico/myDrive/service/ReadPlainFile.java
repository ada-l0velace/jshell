package pt.tecnico.myDrive.service;


import pt.tecnico.myDrive.domain.*;

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
        Manager m = Manager.getInstance();
        Session s = m.getSessionByToken(_plainFileToken);
        Directory cd = s.getCurrentDirectory();
        PlainFile pf;
    	if((pf = (PlainFile) cd.searchFile(_plainFileName, _plainFileToken)) instanceof PlainFile){
    		_content = pf.getContent(s.getUser());
            return;
    	}
        throw new InvalidFileTypeException("Not a plainFile");

    }
    
    public String result() {
        return _content;
    }
//so para commit
}
