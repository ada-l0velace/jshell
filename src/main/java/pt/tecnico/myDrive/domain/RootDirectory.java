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
        init(owner,name, null, m);
        //owner.createLink(this, "..", this, m);
        //owner.createLink(this, ".", this, m);
    }

    /**
     * Initiates the data in the object
     * @param owner (User) represents the owner of the File.
     * @param name  (String) represents the name of the File.
     * @param parent (Directory) represents parent directory.
     * @param m (Manager) represents an instance of Manager.
     */
    protected void init(User owner, String name, Directory parent, Manager m) {
        super.init(owner, name, parent, m);
        m.setHome(this);
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
