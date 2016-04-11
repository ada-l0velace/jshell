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

public class ListDirectory extends MyDriveService {

	private List<FileDto> _files;
    private String _directoryToken;
    private Directory _currentDir;

    public ListDirectory(String token) {
    	_directoryToken = token;
    }

    public final void dispatch() throws MyDriveException {
    	_currentDir = Manager.getInstance().getSessionByToken(_directoryToken).getCurrentDirectory();
        _files = new ArrayList<FileDto>();

        for (File f : _currentDir.getFileSet()) {
	    if (f instanceof PlainFile)
	    	_files.add(new PlainFileDto(f.getId(), f.getName(), f.getModified(), f.getPermissions().getUmask(),
	                   f.getParent().getName(), f.getOwner().getName(), f.getContent()));
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