package pt.tecnico.myDrive.domain;
import org.jdom2.Element;

public class Link extends Link_Base {
    
    public Link() {
        super();
    }

    @Override
    public void importXml () {
        super.importXml();
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
