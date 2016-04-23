package pt.tecnico.myDrive.service.factory;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.DirectoryContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.LinkEmptyContentException;

/**
 * Created by lolstorm on 19/04/16.
 */
public abstract class FileFactory implements Factory {

    protected String _token;

    protected Manager _manager;
    protected Session _session;
    protected User _creator;
    protected Directory _parent;

    public FileFactory() {
        super();
    }

    public FileFactory(String token) {
        this();
        _token = token;
        _manager = Manager.getInstance();
        _session = _manager.getSessionByToken(token);
        _creator = _session.getUser();
        _parent = _session.getCurrentDirectory();
    }

    /**
     * File creation method.
     * @param fileName (String) receives the name of the file.
     * @param content (String) receives the content of the file.
     * @return Instance of the file specified.
     * @throws InvalidFileTypeException occurs when the file type is incorrect.
     * @throws DirectoryContentException occurs when trying to create a directory with content.
     */
    public abstract File CreateFile(
            String fileName,
            String content) throws DirectoryContentException ;
        /*
        switch (type) {
            case DIRECTORY:
                return new Directory(_creator, fileName, _parent, m);
            case PLAINFILE:
                return new PlainFile(_creator, fileName, content, _parent, m);
            case LINK:
                File fileToLink = _creator.getFileByPath(content, _token);
                return new Link(fileName, fileToLink, content, _parent, m);
            case APP:
                return new App(_creator, fileName, content, _parent, m);
            default:
                throw new InvalidFileTypeException(type.name());
        }*/

}
