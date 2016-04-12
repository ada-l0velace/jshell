package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;
import pt.tecnico.myDrive.service.dto.PlainFileDto;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Permissions;
import pt.tecnico.myDrive.domain.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ListDirectory extends LoginRequiredService {

	private List<FileDto> _files;
    private Session _session;
    private String _token;

    public ListDirectory(String token) {
    	super(token);
    	_token = token;
    	_session = Manager.getInstance().getSessionByToken(token);
    }

    @Override
    public final void dispatch() throws MyDriveException {
    	super.dispatch();
    	Directory currentDir = _session.getCurrentDirectory();
        _files = new ArrayList<FileDto>();

        for (File f : currentDir.getFileSet()) {
	    if (f instanceof PlainFile)
	    	_files.add(new PlainFileDto(f.getId(), f.getName(), f.getModified(), f.getPermissions().getUmask(),
	                   f.getParent().getName(), f.getOwner().getName(), f.getContent(Manager
                                                                                     .getInstance().getUserByToken(_token))));
	    else
	    	_files.add(new FileDto(f.getId(), f.getName(), f.getModified(), f.getPermissions().getUmask(),
	                   f.getParent().getName(), f.getOwner().getName()));
        }

        Collections.sort(_files);
    }

    public final List<FileDto> result() {
        return _files;
    }
}
