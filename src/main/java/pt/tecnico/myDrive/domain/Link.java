package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.visitor.IElementXml;
import pt.tecnico.myDrive.visitor.IVisitorImportXml;


public class Link extends Link_Base implements IElementXml {
    
    public Link() {
        super();
    }
    
    @Override
    public void importXml (IVisitorImportXml v) {
        v.importXml(this);
    }
}
