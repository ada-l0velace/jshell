package pt.tecnico.myDrive.service.factory;

import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.File;

/**
 * Created by lolstorm on 22/04/16.
 */
public class AppFactory extends FileFactory {

    public AppFactory(String token) {
        super(token);
    }

    public File CreateFile(String fileName,
                           String content) {

        return new App(_creator, fileName, content, _parent, _manager);
    }
}
