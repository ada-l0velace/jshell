package pt.tecnico.myDrive.domain;
import org.jdom2.Element;


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
    
    @Override
    public String toString(){
    	return getContent();
    }
}
