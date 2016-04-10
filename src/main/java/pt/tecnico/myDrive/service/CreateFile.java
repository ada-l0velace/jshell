package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;

/**
 * Created by lolstorm on 09/04/16.
 */
public class CreateFile extends MyDriveService {

    private User _user;
    private Session _session;
    private String _filename;
    private String _fileType;
    private String _content;

    public CreateFile(String token, String fileName, String fileType) {
        super();
        _user = Manager.getInstance().getUserByToken(token);
        _session = Manager.getInstance().getSessionByToken(token);
        _filename = fileName;
        _fileType = fileType;
    }

    public CreateFile(String token, String fileName, String fileType, String content) {
        this(token, fileName, fileType);
        _content = content;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        Directory p = _session.getCurrentDirectory();
        Manager m = Manager.getInstance();
        switch (_fileType) {
            case "L":
                File f = _user.getFileByPath(_content);
                new Link(_filename, f, _content, p, m);
                break;
            case "P":
                new PlainFile(_user, _filename, _content, p, m);
                break;
            case "D":
                new Directory(_user, _filename, p, m);
                break;
            case "A":
                new App(_user, _filename, _content, p, m);
                break;
            default:
                throw new InvalidFileTypeException(_fileType);
        }
    }
}
