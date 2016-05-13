package pt.tecnico.myDrive.service;

import java.util.Arrays;
import pt.tecnico.myDrive.domain.*;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.ExecuteFileException;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.UserSessionExpiredException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;

public class ExecuteFileService extends LoginRequiredService {

	private String _sessionToken;
	private String _path;
	private String [] _args;

	public ExecuteFileService(String token, String path) {
		super(token);
		this._sessionToken = token;
		this._path = path;
		this._args = new String[]{};
	}
	
	public ExecuteFileService(String token, String path, String [] args){
		super(token);
		this._sessionToken = token;
		this._path = path;
		this._args = args;
	}

	@Override
    protected void dispatch()  throws MyDriveException{
		super.dispatch();
		Manager m = Manager.getInstance();
		User u = m.getUserByToken(_sessionToken);
		File f = u.getFileByPath(_path, _sessionToken);
		f.execute(_sessionToken, _args);
	}
}
