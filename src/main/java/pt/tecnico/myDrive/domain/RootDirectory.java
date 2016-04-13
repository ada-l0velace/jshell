package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.DeleteRootDirectoryException;

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
        init(owner,name, this, m);
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
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Element xmlExport(){
        return super.xmlExport();
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

    public void remove(User user) throws DeleteRootDirectoryException {
        throw new DeleteRootDirectoryException(this.getName());
    }
}
