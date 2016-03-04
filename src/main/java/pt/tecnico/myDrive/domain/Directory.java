package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.visitor.IElementXml;
import pt.tecnico.myDrive.visitor.IVisitorImportXml;


public class Directory extends Directory_Base implements IElementXml {
    
    public Directory() {
        super();
    }
    
    @Override
    public void importXml (IVisitorImportXml v) {
        v.importXml(this);
    }
}
