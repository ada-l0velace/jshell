package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.visitor.IElementXml;
import pt.tecnico.myDrive.visitor.IVisitorImportXml;


public class PlainFile extends PlainFile_Base implements IElementXml {
    
    public PlainFile() {
        super();
    }
    
    @Override
    public void importXml (IVisitorImportXml v) {
        v.importXml(this);
    }
}
