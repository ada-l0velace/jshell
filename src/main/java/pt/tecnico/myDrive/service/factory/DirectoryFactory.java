package pt.tecnico.myDrive.service.factory;

import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.exception.DirectoryContentException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;

/**
 * Created by lolstorm on 22/04/16.
 */
public class DirectoryFactory extends FileFactory {

    public DirectoryFactory(String token) {
        super(token);
    }

    public File CreateFile(String fileName, String content) throws DirectoryContentException {
        if (!content.equals(""))
            throw new DirectoryContentException();
        return new Directory(_creator, fileName, _parent, _manager);
    }
}
