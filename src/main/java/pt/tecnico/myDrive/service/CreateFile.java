package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.filefactory.AbstractFactory;
import pt.tecnico.myDrive.filefactory.AbstractFactory.FileType;
import pt.tecnico.myDrive.filefactory.FileFactory;

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
        Directory p = _session.getCurrentDirectory();
        Manager m = Manager.getInstance();
        AbstractFactory fileFactory = new FileFactory();
        fileFactory.CreateFile(_filename, _fileType, _content, p, _user, _session.getToken());
    }
}
