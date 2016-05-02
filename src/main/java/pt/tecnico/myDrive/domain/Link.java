package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pt.tecnico.myDrive.exception.InvalidNameFileException;
import pt.tecnico.myDrive.service.dto.FileDto;

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

    /**
     * Sets the content of a PlainFile.
     * @param cont (String) receives a content for the file.
     * @throws InvalidNameFileException occurs when the content length is over 1024 characters.
     */
    @Override
    public void initContent(String cont) throws InvalidNameFileException {
    	if (cont.length() > 1024){
    		throw new InvalidNameFileException(cont);
    	}
    	else 
    		super.initContent(cont);
    }

    /**
     * Gets content from the linked file.
     * @return (String) returns the content of the linked file.
     */
    @Override
    public String getContent(String token) {
        readPermissions (token);
        User u = Manager.getInstance().getUserByToken(token);
        File f = u.getFileByPath(getContentAux(), token);
        return f.getContent(token);
    }

    /**
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of File.
     */
    public String toString(){
    	String a = super.toString();
	    return a + " -> " + getContentAux();
    }
}
