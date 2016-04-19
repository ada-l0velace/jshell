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

    abstract public File CreateFile(
            String fileName,
            FileType type,
            String content,
            Directory parent,
            User creator,
            String token) throws InvalidFileTypeException, DirectoryContentException;
}
