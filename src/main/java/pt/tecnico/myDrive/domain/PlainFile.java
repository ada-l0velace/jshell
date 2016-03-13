package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.io.UnsupportedEncodingException;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import org.joda.time.format.ISODateTimeFormat;


/**
 * 
 */
public class PlainFile extends PlainFile_Base {

    /**
     * Default constructor to create PlainFile
     */
    public PlainFile() {
        super();
    }

    /**
     * Alternate construtor to create a File with xml.
     * @param  xml Element (JDOM library type) which represents a File.
     */
    public PlainFile(Element xml, User owner) {
        super();
        importXml(xml);
        setOwner(owner);
    }

    /**
     * Alternate construtor to create a PlainFile.
     * @param owner User (JDOM library type) which represents a User.
     * @param name String (Java Primitive) which represents the name a File.
     */
    public PlainFile(User owner, String name, String content, Manager m) {
        super();
        super.init(owner, name, m);
        setContent(content);
    }

    /**
     * Imports a PlainFile from persistent state (XML format).
     * @throws ImportDocumentException
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
     * @return Element (JDOM library type) which represents a PlainFile
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
    
    public String getDim() throws RuntimeException {
        String dim = "";
        try {
            dim = getContent().getBytes("UTF-8").length + "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return dim;
    }
    
    public String toString() {
    	String a = super.toString();
    	String dim = getDim(); 	
    	String username = getOwner().getUsername();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        String modified = fmt.print(getModified());
        
        String rest = dim + "kb " + username + " " + this.getId() + " " + modified + " " + this.getName();
        return a + rest;
    }
}
