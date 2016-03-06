package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.interfaces.IElementXml;
import org.jdom2.Element;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

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

	/**
	 * Exports a file to persistent state (XML format)
	 * @see User
	 * @return Element (JDOM library type) which represents a File 
	 */
    @Override
    public Element exportXml () {
		Element node = new Element("file");
		node.setAttribute("id", Integer.toString(getId()));
		node.setAttribute("name", getName());

		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		node.setAttribute("modifier", fmt.print(getModified()));
		
		Element perm = new Element("permissions");
		perm.setAttribute("umask", Short.toString(getPermissions().getUmask()));

		node.addContent(perm);

		node.addContent(getOwner().exportXml());

		return node;
    }
}
