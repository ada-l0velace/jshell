package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.DeletePermissionException;
import pt.tecnico.myDrive.exception.SpecialDirectoriesException;

public class DeleteFile extends LoginRequiredService {

    private User _user;
    private Session _session;
    private String _filename;
    private String _token;

    public DeleteFile(String token, String fileName) {
        super(token);
        _token = token;
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
        _filename = fileName;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
        Directory dir = _session.getCurrentDirectory();
        if(_filename.equals("..") || _filename.equals(".")) 
            throw new SpecialDirectoriesException(_user.getUsername());
        File file = dir.searchFile(_filename, _token);
        log.trace(file+" wtffffffff<<<<<<<<<<<");
        if(file != null) {
            file.remove(_token);
        }
    }
}
