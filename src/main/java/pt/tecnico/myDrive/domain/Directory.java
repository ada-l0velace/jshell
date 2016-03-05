package pt.tecnico.myDrive.domain;
import org.jdom2.Element;


public class Directory extends Directory_Base {
    
    public Directory() {
        super();
    }
    
    @Override
    public void importXml () {
        super.importXml();
    }

    @Override
    public Element exportXml () {
        throw new UnsupportedOperationException("Not Implemented!");
    }
}
