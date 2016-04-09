package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;


public class ListDirectory extends MyDriveService {

    public ListDirectory(String token) {

    }

    @Override
    protected void dispatch() throws MyDriveException {

    }

    public final List<FileDto> result() {
        return null;
    }
}