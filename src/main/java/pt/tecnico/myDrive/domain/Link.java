package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.UnsupportedEncodingException;
import pt.tecnico.myDrive.exception.InvalidNameFileException;

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
     * @param parent (Directory) which represents the parent directory of the link.
     * @param m (Manager) which represents the Manager
     */
    public Link(String name, File file, String path, Directory parent, Manager m){
        super();
        setLastId(m);
        setName(name);
        setModified(new DateTime(DateTimeZone.UTC));
        setPermissions(new Permissions(file.getPermissions().getUmask()));
        setOwner(file.getOwner());
        initContent(path);
        setParent(parent);
    }

    /**
     * Alternate construtor to create a Link with xml.
     * @param  xml Element (JDOM library type) which represents a File.
     */
    public Link(Element xml) {
        super();
        importXml(xml);
    }
    
    @Override
    public void initContent(String cont) throws InvalidNameFileException{
    	if (cont.length() > 1024){
    		throw new InvalidNameFileException(cont);
    	}
    	else 
    		super.initContent(cont);
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
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Element xmlExport(){
        return super.xmlExport();
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
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of File.
     */
    public String toString(){
    	String a = super.toString();
	    return a + " -> " + this.getContent();
    }
}
