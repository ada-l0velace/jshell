package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.visitor.IElementXml;
import pt.tecnico.myDrive.visitor.IVisitorImportXml;

public class App extends App_Base implements IElementXml {
    
    public App() {
        super();
    }

    @Override
    public void importXml (IVisitorImportXml v) {
        v.importXml(this);
    }
    
}
