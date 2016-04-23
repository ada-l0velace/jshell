package pt.tecnico.myDrive.service.factory;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;

/**
 * Created by lolstorm on 23/04/16.
 */
public class FileFactoryProducer {
    public static Factory getFactory(Factory.FileType type, String token) {
        switch (type) {
            case DIRECTORY:
                return new DirectoryFactory(token);
            case PLAINFILE:
                return new PlainFileFactory(token);
            case LINK:
                return new LinkFactory(token);
            case APP:
                return new AppFactory(token);
            default:
                throw new InvalidFileTypeException(type.name());
        }
    }
}
