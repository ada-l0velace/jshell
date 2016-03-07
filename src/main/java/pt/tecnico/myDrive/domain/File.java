package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.interfaces.IElementXml;

import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import pt.tecnico.myDrive.domain.User;


/**
 * 
 */
public class File extends File_Base implements IElementXml {
    
    private Element _xml;

    /**
     * Default construtor to create File. 
     */
    public File() {
        super();
    }
    
    /**
     * Alternate construtor to create a File with xml.
     * @param Element (JDOM library type) which represents a File.
     */
    public File(Element xml){
        _xml = xml;
        importXml();
    }

    public Element getXml() {
        return _xml;
    }

    /**
     * Imports a File from persistent state (XML format).
     * @see User Permissions
     */
    @Override
    public void importXml () { 
        Element node = getXml();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

        int id = Integer.parseInt(node.getAttribute("id").getValue());
        String name = node.getAttribute("name").getValue();
        DateTime dateModified = new DateTime(
            fmt.parseDateTime(
                node.getAttribute("modified").getValue())
            );
        
        Element permission = node.getChild("permissions");
        short umask = Short.parseShort(permission.getAttribute("umask").getValue());
        
        Element ownerXml = node.getChild("owner");
        User owner = new User(ownerXml);

        setId(id);
        setName(name);
        setModified(dateModified);
        setPermissions(new Permissions(umask));
        setOwner(owner);
    }

	/**
	 * Exports a file to persistent state (XML format)
	 * @see User
	 * @see Permissions
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
    
    @Override
    public File getFileByPath (String link){
    	throw new UnsupportedOperationException("Not Implemented!");
    }
}
