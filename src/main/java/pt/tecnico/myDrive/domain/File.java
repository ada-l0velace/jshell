package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.interfaces.IElementXml;
import org.jdom2.Element;

/**
 * 
 */
public class File extends File_Base implements IElementXml {
    
    public File() {
        super();
    }
    
    /**
     * Imports a File from persistent state (XML format),
     * building it again.
     * This method builds only File type objects.
     */
    @Override
    public void importXml () {
        throw new UnsupportedOperationException("Not Implemented!");
    }

    @Override
    public Element exportXml () {
        throw new UnsupportedOperationException("Not Implemented!");
    }
}
