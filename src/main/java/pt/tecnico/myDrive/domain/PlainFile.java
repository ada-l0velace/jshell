package pt.tecnico.myDrive.domain;
import org.jdom2.Element;



public class PlainFile extends PlainFile_Base {
    
    public PlainFile() {
        super();
    }

    @Override
    public void importXml () {
        super.importXml();
    }

    @Override
    public Element exportXml () {
		Element node = super.exportXml();
		node.setAttribute("content", getContent());
		return node;
    }
    public File getFileByPath (String link){
    	return this;
    }
}
