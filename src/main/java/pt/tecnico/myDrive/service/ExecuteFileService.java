package pt.tecnico.myDrive.service;

import java.util.Arrays;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.ExecuteFileException;

public class ExecuteFileService extends LoginRequiredService {

	private String _sessionToken;
	private String _path;
	private String [] _args;

	public ExecuteFileService(String token, String path) {
		super(token);
		_sessionToken = token;
		_path = path;
		_args = new String[0];
	}
	
	public ExecuteFileService(String token, String path, String [] args){
		super(token);
		_sessionToken = token;
		_path = path;
		_args = args;
	}

	@Override
    protected void dispatch() throws MyDriveException{
		super.dispatch();
		Manager m = Manager.getInstance();
		User u = m.getUserByToken(_sessionToken);
		File f = u.getFileByPath(_path, _sessionToken);
		f.execute(_sessionToken, _args);
	}
}
