package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import java.io.UnsupportedEncodingException;
import pt.tecnico.myDrive.exception.ImportDocumentException;


public class PlainFile extends PlainFile_Base {

    /**
     * Default constructor to create PlainFile
     */
    public PlainFile() {
        super();
    }

    /**
     * Alternate constructor to create a File with xml.
     * @param  xml (Element JDOM) which represents a File.
     */
    public PlainFile(Element xml, User owner) {
        super();
        importXml(xml);
        setOwner(owner);
    }

    /**
     * Alternate constructor to create a PlainFile.
     * @param owner (User) which represents a User.
     * @param name (String) which represents the name a File.
     */
    public PlainFile(User owner, String name, String content, Manager m) {
        super();
        super.init(owner, name, m);
        setContent(content);
    }

    /**
     * Imports a PlainFile from persistent state (XML format).
     * @throws ImportDocumentException occurs when there is an error in import.
     */
    @Override
    public void importXml (Element xml) {
        Element node = xml;
        super.importXml(node);
        String content = node.getAttribute("content").getValue();
        setContent(content);
    }


    /**
     * Exports a PlainFile to a persistent state (XML format),
     * @see File
     * @return (Element JDOM) which represents a PlainFile in xml format.
     */
    @Override
    public Element exportXml () {
        Element node = super.exportXml();
        node.setAttribute("content", getContent());
        return node;
    }


    /**
     * @param  String link receives a String with the link content. 
     * @return  File  returns himself.
     */
    public File getFileByPath (String link){
        return this;
    }

    /**
     * Gets directory dimension.
     * @return String represents the directory dimension in string.
     * @throws RuntimeException occurs when there is an error in an unicode character.
     */
    public String getDim() throws RuntimeException {
        String dim;
        try {
            dim = getContent().getBytes("UTF-8").length + "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return dim;
    }

    /**
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of PlainFile.
     */
    public String toString() {
    	String a = super.toString();
    	String dim = getDim(); 	
    	String username = getOwner().getUsername();
        String modified = getModified().toString("MMM dd hh:mm");
        String rest = dim + " " + username + " " + getId() + " " + modified + " " + this.getName();
        return a + rest;
    }
}
