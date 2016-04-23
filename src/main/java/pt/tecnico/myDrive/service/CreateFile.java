package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.factory.Factory;
import pt.tecnico.myDrive.service.factory.Factory.FileType;
import pt.tecnico.myDrive.service.factory.FileFactory;
import pt.tecnico.myDrive.service.factory.FileFactoryProducer;

/**
 * Created by lolstorm on 09/04/16.
 */
public class CreateFile extends LoginRequiredService {

    private User _user;
    private Session _session;
    private String _filename;
    private FileType _fileType;
    private String _content;

    public CreateFile(String token, String fileName, FileType fileType) {
        super(token);
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
        _filename = fileName;
        _fileType = fileType;
        _content = "";
    }

    public CreateFile(String token, String fileName, FileType fileType, String content) {
        this(token, fileName, fileType);
        _content = content;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
        Factory fileFactory = FileFactoryProducer.getFactory(_fileType, _session.getToken());
        fileFactory.CreateFile(_filename, _content);
    }
}
