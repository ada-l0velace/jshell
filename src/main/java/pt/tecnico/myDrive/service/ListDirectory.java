package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;
import pt.tecnico.myDrive.service.dto.DirectoryDto;
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
    	int a = 2;
    	Directory currentDir = _session.getCurrentDirectory();
    	Directory parent = currentDir.getParent();
        _files = new ArrayList<FileDto>();
        
        for (File f : currentDir.listContent(_token)) {
	        if (!(f.getName().equals("/"))){
			    if (!(f instanceof Directory))
			    	_files.add(new PlainFileDto(f.getId(), f.getName(), f.getModified(), f.getPermissions().getUmask(),
			    								f.getParent().getName(), f.getOwner().getName(),
			    								f.getContent(_token), f.toString()));
			    else
			    	_files.add(new DirectoryDto(f.getId(), f.getName(), f.getModified(), f.getPermissions().getUmask(),
			                   f.getParent().getName(), f.getOwner().getName(), f.toString()));
		        }
	        else 
	        	a = 1;
        }
        
        String op1 = "D" + " " + currentDir.getPermissions().toString() + " " + 
        			(currentDir.listContent(_token).size()+a) + " " + currentDir.getOwner().getUsername() + " " + 
        			currentDir.getId() + " " + currentDir.getModified().toString("MMM dd hh:mm") + " .";
        
        String op2 = "D" + " " + parent.getPermissions().toString() + " " + 
    			(parent.listContent(_token).size()+a) + " " + parent.getOwner().getUsername() + " " + 
    			parent.getId() + " " + parent.getModified().toString("MMM dd hh:mm") + " ..";
        
        _files.add(new DirectoryDto(currentDir.getId(), ".", currentDir.getModified(), 
        		currentDir.getPermissions().getUmask(), parent.getName(), currentDir.getOwner().getUsername(), op1));
        
        _files.add(new DirectoryDto(parent.getId(), "..", parent.getModified(), parent.getPermissions().getUmask(), 
        		parent.getParent().getName(), parent.getOwner().getUsername(), op2));

        Collections.sort(_files);
    }

    public String output(){
    	String output = "";
    	for (FileDto f : result()) {
    		output = output + f.toString() + "\n";
    	}
    	return output;
    }
    
    public final List<FileDto> result() {
        return _files;
    }
}
