package pt.tecnico.myDrive.service.factory;

import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.DirectoryContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;

/**
 * Created by lolstorm on 19/04/16.
 */
public interface Factory {

    enum FileType {
        DIRECTORY, PLAINFILE, LINK, APP, UNKNOWN
    }

    /**
     * Interface file creation method.
     * @param fileName (String) receives the name of the file.
     * @param content (String) receives the content of the file.
     * @return Instance of the file specified.
     * @throws InvalidFileTypeException occurs when the file type is incorrect.
     * @throws DirectoryContentException occurs when trying to create a directory with content.
     */
     File CreateFile(
            String fileName,
            String content) throws InvalidFileTypeException, DirectoryContentException;
}
