package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.WritePermissionException;


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
        User u = Manager.getInstance().getUserByToken(_token);
    	if(u.getFileByPath(_plainFileName, _token) instanceof PlainFile) {
            Directory d = u.getSessionDirectory(_token);
            PlainFile f = (PlainFile) d.searchFile(_plainFileName, _token);
            log.error(_user.getPermissions().canWrite(f));
            if(_user.getPermissions().canWrite(f))
                f.setContent(_content);
            else
                throw new WritePermissionException(f.getName(), _user.getUsername());
            return;
        }
        throw new InvalidFileTypeException("not a plainFile");
        
    }

}
