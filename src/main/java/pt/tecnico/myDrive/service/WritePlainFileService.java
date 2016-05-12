package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.WritePermissionException;


public class WritePlainFileService extends LoginRequiredService {

    private String _path;
    private String _content;
    private String _token;


    public WritePlainFileService(String token, String path) {
        this(token, path,"");
    }

    public WritePlainFileService(String token, String path, String content) {
    	super(token);
        _token = token;
        _path = path;
        _content = content;
    }

    @Override
    protected void dispatch() throws MyDriveException {
    	super.dispatch();
        User u = Manager.getInstance().getUserByToken(_token);
    	if(u.getFileByPath(_path, _token) instanceof PlainFile) {
            Directory d = u.getSessionDirectory(_token);
            PlainFile f = (PlainFile) d.searchFile(_path, _token);
            f.setContent(_content, _token);
            return;
        }
        throw new InvalidFileTypeException("not a plainFile");
        
    }

}
