package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Session;

import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;


public class ChangeDirectory extends LoginRequiredService {

    private String _sessionToken;
    private String _path;
    
    public ChangeDirectory(String token, String path){
        super(token);
        this._sessionToken = token;
        this._path = path; 
    }

    @Override
    protected void dispatch() throws FileNotFoundException, UserSessionExpiredException{
        super.dispatch();
        Session s = Manager.getInstance().getSessionByToken(_sessionToken);
        Directory d = s.getCurrentDirectory();
        if (this._path == ".")
            this._path = d.getPath() + d.getName();
        else if (this._path == "..")
            this._path = d.getPath();
        else if (!this._path.startsWith("/"))
            this._path = d.getPath() + d.getName() + this._path;

        s.setCurrentDirectory((Directory) s.getUser().getFileByPath(this._path));
    }

    public String result(){
        return this._sessionToken;
    }
}
