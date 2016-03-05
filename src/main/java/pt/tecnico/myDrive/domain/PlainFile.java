package pt.tecnico.myDrive.domain;


public class PlainFile extends PlainFile_Base {
    
    public PlainFile() {
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
