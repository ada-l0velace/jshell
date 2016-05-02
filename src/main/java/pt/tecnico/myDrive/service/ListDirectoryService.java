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

public class ListDirectoryService extends LoginRequiredService {

	private List<FileDto> _files;
    private Session _session;
    private String _token;

    public ListDirectoryService(String token) {
    	super(token);
    	_token = token;
    	_session = Manager.getInstance().getSessionByToken(token);
    }
    
    @Override
    public final void dispatch() throws MyDriveException {
    	super.dispatch();
    	Directory currentDir = _session.getCurrentDirectory();
    	_files = new ArrayList<>();
        currentDir.listContent(_token).forEach(f -> {
            if (!f.getName().equals("/"))
                _files.add(f.getDtoData(_token)); });
        _files.add(currentDir.getDotData(_token));
        _files.add(currentDir.getDotDotData(_token));
    }

    public String output(){
    	String output = "";
    	for (FileDto f : result()) {
    		output = output + f.toString() + "\n";
    	}
    	return output;
    }
    
    public final List<FileDto> result() {
		Collections.sort(_files);
		return _files;
    }
}
