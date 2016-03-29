package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

public class RootDirectory extends RootDirectory_Base {

    /**
     * Default Constructor.
     */
    public RootDirectory() {
        super();
    }

    /**
     * Alternate Constructor for a RootDirectory.
     * @param  owner User user owner of the file.
     * @param name Represents the name of the folder.
     */
    public RootDirectory(User owner, String name, Manager m) {
        super();
        super.nameRegex = "[^\0]*";
        super.init(owner, name, null, m);
        //setParent(parent);
        addFile(owner.createLink(this, ".."));
        addFile(owner.createLink(this, "."));
    }

    /**
     * Exports the root Directory to a persistent state (XML format),
     * @see Directory
     * @return Element (JDOM library type) which represents the root directory
     */
    @Override
    public Element exportXml () {
        return super.exportXml();
    }
}
