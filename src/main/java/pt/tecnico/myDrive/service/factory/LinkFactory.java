package pt.tecnico.myDrive.service.factory;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.LinkEmptyContentException;

/**
 * Created by lolstorm on 22/04/16.
 */
public class LinkFactory extends FileFactory  {

    public LinkFactory(String token) {
        super(token);
    }

    public File CreateFile(String fileName,
                           String content) {
        if (content.equals(""))
            throw new LinkEmptyContentException();
        File fileToLink = _creator.getFileByPath(content, _token);
        return new Link(fileName, fileToLink, content, _parent, _manager);
    }
}
