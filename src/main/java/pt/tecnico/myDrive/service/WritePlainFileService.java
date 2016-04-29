package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.WritePermissionException;


public class WritePlainFileService extends LoginRequiredService {
	
	private User _user;
    private Session _session;
    private String _plainFileName;
    private String _content;
    private String _token;

    public WritePlainFileService(String token, String plainFileName, String content) {
    	super(token);
        _token = token;
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
        _plainFileName = plainFileName;
        _content = content;
    }

    @Override
    protected void dispatch() throws MyDriveException {
    	super.dispatch();
        User u = Manager.getInstance().getUserByToken(_token);
    	if(u.getFileByPath(_plainFileName, _token) instanceof PlainFile) {
            Directory d = u.getSessionDirectory(_token);
            PlainFile f = (PlainFile) d.searchFile(_plainFileName, _token);
            f.setContent(_content, _token);
            return;
        }
        throw new InvalidFileTypeException("not a plainFile");
        
    }

}