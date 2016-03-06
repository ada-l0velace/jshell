package pt.tecnico.myDrive.interfaces;
import org.jdom2.Element;
import pt.tecnico.myDrive.domain.*;

/**
 * 
 */
public interface IElementXml {
    public void importXml ();
    public Element exportXml ();
    public File getFileByPath (String link);
}
