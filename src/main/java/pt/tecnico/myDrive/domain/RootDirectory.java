package pt.tecnico.myDrive.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import pt.tecnico.myDrive.exception.InvalidNameFileException;

public class RootDirectory extends RootDirectory_Base {
    
    public RootDirectory() {
        super();
    }

    /**
     * Alternate Constructor for a RootDirectory.
     * @param  owner User user owner of the file.
     * @param name Represents the name of the folder.
     */
    public RootDirectory(User owner, String name, Link parent) {
        super();
        super.nameRegex = "[^\0]*";
        super.init(owner, name);
        //setParent(parent);
        addFile(new Link ("..", this));
    }
    
}
