package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;

import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;


public class ChangeDirectoryService extends LoginRequiredService {

    private String _sessionToken;
    private String _path;
    
    public ChangeDirectoryService(String token, String path){
        super(token);
        this._sessionToken = token;
        this._path = path; 
    }

    @Override
    protected void dispatch() throws FileNotFoundException, UserSessionExpiredException, InvalidFileTypeException{
        super.dispatch();
        Session s = Manager.getInstance().getSessionByToken(_sessionToken);
        Directory d = s.getCurrentDirectory();

        if (!(this._path.startsWith("/") || this._path.isEmpty() || this._path.equals(".") || this._path.equals("..")))
            this._path = d.getPath() + d.getName() + "/" + this._path;
        if (s.getUser().getFileByPath(this._path, _sessionToken) instanceof Directory)
            s.setCurrentDirectory( (Directory) s.getUser().getFileByPath(this._path, _sessionToken));
        else
            throw new InvalidFileTypeException(s.getUser().getFileByPath(this._path, _sessionToken).getName());
    }

    public String result(){
        return _path;
    }
}
