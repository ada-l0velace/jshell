package pt.tecnico.myDrive.filefactory;

import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.DirectoryContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;

/**
 * Created by lolstorm on 19/04/16.
 */
public abstract class AbstractFactory {

    public enum FileType {
        DIRECTORY, PLAINFILE, LINK, APP, UNKNOWN
    }

    /**
     * Abstract file creation method.
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
    abstract public File CreateFile(
            String fileName,
            FileType type,
            String content,
            Directory parent,
            User creator,
            String token) throws InvalidFileTypeException, DirectoryContentException;
}
