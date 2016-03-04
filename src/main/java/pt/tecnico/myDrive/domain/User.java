package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.visitor.IElementXml;
import pt.tecnico.myDrive.visitor.IVisitorImportXml;


public class User extends User_Base implements IElementXml {
    
    public User() {
        super();
    }
    
    @Override
    public void importXml (IVisitorImportXml v) {
        v.importXml(this);
    }
}
