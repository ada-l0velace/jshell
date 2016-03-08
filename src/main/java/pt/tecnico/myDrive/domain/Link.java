package pt.tecnico.myDrive.domain;
import org.jdom2.Element;


public class Link extends Link_Base {
    
    /**
     * Default Constructor
     */
    public Link() {
        super();
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

	public File getFileByPath (String link){
		throw new UnsupportedOperationException("Not Implemented!");
	}
}
