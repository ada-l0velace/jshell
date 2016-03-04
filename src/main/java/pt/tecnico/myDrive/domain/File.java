package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.visitor.IElementXml;
import pt.tecnico.myDrive.visitor.IVisitorImportXml;


public class File extends File_Base implements IElementXml {
    
    public File() {
        super();
    }
    
    @Override
    public void importXml (IVisitorImportXml v) {
        v.importXml(this);
    }
}
