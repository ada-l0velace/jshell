package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;


public class WritePlainFile extends LoginRequiredService {
	
	private User _user;
    private Session _session;
    private String _plainfilename;
    private String _content;

    public WritePlainFile(String token, String plainfilename, String content) {
    	super(token);
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
        _plainfilename = plainfilename;
        _content = content;
    }

    @Override
    protected void dispatch() throws MyDriveException {
    	super.dispatch();
    	/*
    	File f = _session.getCurrentDirectory().searchFile(_plainfilename);
        f.setContent(_content);
        */
    }
}
