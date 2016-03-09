package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.domain.Permissions;
import pt.tecnico.myDrive.interfaces.IElementXml;

import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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
	 * Alternative construtor to create a File.
	 * @param  id       int unique ID of the file.
	 * @param  name     String name of the file.
	 * @param  modified DateTime date when it was modified.
	 * @param  umask int umask for permissions.
	 */
	public File(User owner) {
		init(owner);
	}
    
	/**
	 * Alternate construtor to create a File with xml.
	 * @param xml Element (JDOM library type) which represents a File.
	 */
	public File(Element xml){
        super();
		importXml(xml);
	}

	public void init (User owner) {
		setId(1);
		setName(owner.getName());
		setModified(new DateTime(DateTimeZone.UTC));
		setPermissions(owner.getPermissions());
	}

	/**
	 * Imports a File from persistent state (XML format).
     * @param xml Element (JDOM library type) which represents a File.
     * @throws ImportDocumentException occurs when there is an error with the import.
	 * @see User Permissions  
	 */
	@Override
	public void importXml (Element xml) { 
		Element node = xml;
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

		int id = Integer.parseInt(node.getAttribute("id").getValue());
		String name = node.getAttribute("name").getValue();
		DateTime dateModified = new DateTime(fmt.parseDateTime(node.getAttribute("modified").getValue()));
        
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

    /**
     * Removes the file.
     * @throws PermissionDeniedException The user doesn't have the privilege to remove the file.
     */
    public void remove()
    { 
        setOwner(null); 
        deleteDomainObject();
    }
}
