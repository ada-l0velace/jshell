package pt.tecnico.myDrive.interfaces;

import org.jdom2.Element;
import pt.tecnico.myDrive.domain.*;

/**
 *  Xml interface for classes that can search for files and import/export xml.
 */

public interface IElementXml {
    public void importXml (Element xml) /*ImportDocumentException*/;
    public Element exportXml ();
}
