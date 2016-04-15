package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;


public class WritePlainFile extends LoginRequiredService {
	
	private User _user;
    private Session _session;
    private String _plainFileName;
    private String _content;
    private String _token;

    public WritePlainFile(String token, String plainfilename, String content) {
    	super(token);
        _token = token;
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
        _plainFileName = plainfilename;
        _content = content;
    }

    @Override
    protected void dispatch() throws MyDriveException {
    	super.dispatch();
    	
    	if(Manager.getInstance().getSessionByToken(_token).getCurrentDirectory().getFileByPath(_plainFileName) instanceof PlainFile){
            PlainFile pf = (PlainFile)Manager.getInstance().getSessionByToken(_token).getCurrentDirectory().getFileByPath(_plainFileName);
            pf.setContent(_content);
            return;
        }
        throw new InvalidFileTypeException("not a plainFile");
        
    }

    public String result() {
        return _content;
    }
}
