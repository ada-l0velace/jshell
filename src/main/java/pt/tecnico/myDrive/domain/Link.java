package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.UnsupportedEncodingException;

public class Link extends Link_Base {
    
    /**
     * Default Constructor
     */
    public Link() {
        super();
    }

    /**
     * Alternate constructor to create a Link with a path
     * @param name (Java Primitive) which represents the name of the file.
     * @param file (File) which represents a file
     * @param path (Java Primitive) which represents the path of the link.
     * @param m (Manager) which represents the Manager
     */
    public Link(String name, File file, String path, Directory parent, Manager m){
        super();
        setLastId(m);
        setName(name);
        setModified(new DateTime(DateTimeZone.UTC));
        setPermissions(new Permissions(file.getPermissions().getUmask()));    
        setContent(path);
        setOwner(file.getOwner());
    }

    /**
     * Alternate construtor to create a Link with xml.
     * @param  xml Element (JDOM library type) which represents a File.
     */
    public Link(Element xml, User owner) {
        super();
        importXml(xml);
        String userName = xml.getAttribute("owner").getValue();
        User owner_ = Manager.getInstance().getUserByUsername(userName);
        if (owner_ != null)
            setOwner(owner_);
        else
            setOwner(owner);
    }

    /**
     * Imports a Link from persistent state (XML format).
     * @throws ImportDocumentException
     */
    @Override
    public void importXml (Element xml) {
        super.importXml(xml);
    }

    /**
     * Exports a Link to a persistent state (XML format),
     * @see PlainFile
     * @return Element (JDOM library type) which represents a Link
     */
    @Override
    public Element exportXml () {
        return super.exportXml();
    }

    /**
     * Interface method.
     * @throws UnsupportedEncodingException occurs always if called directly with a Link.
     */
    public File getFileByPath (String link){
        throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of File.
     */
    public String toString(){
    	String a = super.toString();
	    return a + " -> " + this.getContent();
    }
}
