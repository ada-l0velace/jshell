package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.interfaces.IElementXml;

public class File extends File_Base implements IElementXml {
    
    public File() {
        super();
    }
    
    @Override
    public void importXml () {
        throw new UnsupportedOperationException("Not Implemented!");
    }

    @Override
    public void exportXml () {
        throw new UnsupportedOperationException("Not Implemented!");
    }
}
