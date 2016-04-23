package pt.tecnico.myDrive.service.factory;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.PlainFile;

/**
 * Created by lolstorm on 22/04/16.
 */
public class PlainFileFactory extends FileFactory {

    public PlainFileFactory(String token) {
        super(token);
    }

    public File CreateFile(String fileName,
                           String content) {
        return new PlainFile(_creator, fileName, content, _parent, _manager);
    }
}
