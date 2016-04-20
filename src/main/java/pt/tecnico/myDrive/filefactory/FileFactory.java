package pt.tecnico.myDrive.filefactory;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.DirectoryContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.LinkEmptyContentException;

/**
 * Created by lolstorm on 19/04/16.
 */
public class FileFactory extends AbstractFactory {

    /**
     * File creation method.
     * @param fileName (String) receives the name of the file.
     * @param type (FileType) receives the name of the file.
     * @param content (String) receives the content of the file.
     * @param parent (Directory) receives the parent of the file.
     * @param creator (User) receives the creator of the file.
     * @param token (String) receives the token to identify the session of the user.
     * @return Instance of the file specified.
     * @throws InvalidFileTypeException occurs when the file type is incorrect.
     * @throws DirectoryContentException occurs when trying to create a directory with content.
     */
    public File CreateFile(
            String fileName,
            FileType type,
            String content,
            Directory parent,
            User creator,
            String token) throws InvalidFileTypeException, DirectoryContentException {
        Manager m = Manager.getInstance();
        if (type == type.DIRECTORY && !content.equals(""))
            throw new DirectoryContentException();
        switch (type) {
            case DIRECTORY:
                return new Directory(creator, fileName, parent, m);
            case PLAINFILE:
                return new PlainFile(creator, fileName, content, parent, m);
            case LINK:
                if (content.equals(""))
                    throw new LinkEmptyContentException();
                File fileToLink = creator.getFileByPath(content, token);
                return new Link(fileName, fileToLink, content, parent, m);
            case APP:
                return new App(creator, fileName, content, parent, m);
            default:
                throw new InvalidFileTypeException(type.name());
        }
    }
}
