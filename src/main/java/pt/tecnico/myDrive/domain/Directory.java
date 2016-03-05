package pt.tecnico.myDrive.domain;


public class Directory extends Directory_Base {
    
    public Directory() {
        super();
    }
    
    @Override
    public void importXml () {
        super.importXml();
    }

    @Override
    public void exportXml () {
        throw new UnsupportedOperationException("Not Implemented!");
    }
}
