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
    public PlainFile(Element xml) {
        super();
        importXml(xml);
    }

    /**
     * Alternate constructor to create a PlainFile.
     * @param owner (User) which represents a User.
     * @param name (String) which represents the name a File.
     */
    public PlainFile(User owner, String name, String content, Directory parent, Manager m) {
        super();
        super.init(owner, name, parent, m);
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
        String content = node.getChild("content").getValue();
        setContent(content);
    }

    /**
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Element xmlExport(){    
        Element node = super.xmlExport();
        node.setAttribute("content", getContent());
        return node;
    }


    /**
     * Exports a PlainFile to a persistent state (XML format),
     * @see File
     * @return (Element JDOM) which represents a PlainFile in xml format.
     */
    @Override
    public Element exportXml () {
        Element node = super.exportXml();

        Element content = new Element("content");
        content.addContent(getContent());

        node.addContent(content);
        
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
