package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.Main;
import pt.tecnico.myDrive.exception.WritePermissionException;
import pt.tecnico.myDrive.exception.DeletePermissionException;
import pt.tecnico.myDrive.interfaces.IElementXml;

import org.jdom2.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import pt.tecnico.myDrive.exception.InvalidNameFileException;

import java.io.UnsupportedEncodingException;


/**
 * Abstract File Class
 */
public abstract class File extends File_Base implements IElementXml {
    
    protected String nameRegex = "[^/\0]*";
    /**
     * Default constructor to create File.
     */
    protected File() {
        super();
    }
    
    /**
     * Alternate constructor to create a File with xml.
     * @param xml Element (JDOM library type) which represents a File.
     */
    protected File(Element xml){
        super();
        importXml(xml);
    }

    /**
     * Protected init Constructor for File
     * @param owner (User) represents the owner of the File.
     * @param name  (String) represents the name of the File.
     * @param m (Manager) represents an instance of Manager.
     */
    protected void init (User owner, String name, Directory parent, Manager m) {
    	setLastId(m);
        setName(name);
        setModified(new DateTime(DateTimeZone.UTC));
        setPermissions(new Permissions(owner.getPermissions().getUmask()));
        setOwner(owner);
        setParent(parent);
    }

    /**
     * Set the last id with the manager
     * @param  m (Manager) represents an instance of Manager.
     */
    public void setLastId(Manager m) {
        int id = m.getLastFileId();
        setId(id+1);
        m.setLastFileId(id+1);
    }

    /**
     * Adds the file to the parent directory.
     * @param d
     */
    @Override
    public void setParent(Directory d) throws WritePermissionException {
        if (d == null)
            super.setParent(null);
        else {
            if (getOwner().getPermissions().CanWrite(d))
                d.addFile(this);
            else
                throw new WritePermissionException(d.getName(), getOwner().getUsername());
        }
    }

    /**
     * Gets the parent absolute path.
     * @return String returns the absolute path string.
     */
    public String getPath() {
        File f = getParent();
        String l = "";
        while (f != null) {
            if (f.getParent() != null)
                l = f.getName()+ "/" + l;
            else
                l = f.getName() + l;
            f = f.getParent();
        }
        return l;
    }

    /**
     * Overrides setName for checking filename validation.
     * @param String (JavaPrimitive) which represents a name for the file.
     * @throws InvalidNameFileException occurs when a file name contains '/' or '\0'.
     * */
    @Override
    public void setName(String name) throws InvalidNameFileException {
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new InvalidNameFileException(name);
        }
        else {
            super.setName(name);
        }
    }
    
    /**
     * Imports a File from persistent state (XML format).
     * @param xml (Element JDOM) which represents a File.
     * @throws ImportDocumentException occurs when there is an error with the import.
     * @see User Permissions  
     */
    @Override
    public void importXml (Element xml) /*ImportDocumentException*/ { 
        Element node = xml;
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

        int id = Integer.parseInt(node.getAttribute("id").getValue());
        String name = new String(node.getChild("name").getValue());
        DateTime dateModified = new DateTime(fmt.parseDateTime(node.getChild("lastModified").getValue()));
        
        short umask = Short.parseShort(node.getChild("perm").getValue());
        
        String userName = node.getChild("owner").getValue();
        User owner = Manager.getInstance().getUserByUsername(userName);
        String path = node.getChild("path").getValue();

        String parentPath = path.substring(0,path.lastIndexOf("/"));
        parentPath = parentPath.equals("") ? "/" : parentPath;

        Directory d = (Directory) owner.getFileByPath(parentPath);

        setLastId(Manager.getInstance());
        setName(name);
        setModified(dateModified);
        setPermissions(new Permissions(umask));
        setOwner(owner);
        setParent(d);

    }

    /**
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Element xmlExport(){
        Element node = new Element(this.getClass().getSimpleName());
        node.setAttribute("id", Integer.toString(getId()));
        node.setAttribute("name", getName());
        node.setAttribute("mask", Short.toString(getPermissions().getUmask()));
        node.setAttribute("owner", getOwner().getUsername());

        Element lastModified = new Element("lastModified");        
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        lastModified.setAttribute("lastModified", fmt.print(getModified()));

        return node;
    }
    
    /**
     * Exports a file to persistent state (XML format)
     * @see User
     * @see Permissions
     * @return Element (JDOM library type) which represents a File 
     */
    @Override
    public Element exportXml () {
        Element node = new Element(this.getClass().getSimpleName());
        node.setAttribute("id", Integer.toString(getId()));
        
        Element name = new Element("name");
        name.addContent(getName());
        Element perm = new Element("perm");
        perm.addContent(Short.toString(getPermissions().getUmask()));
        Element owner = new Element("owner");
        owner.addContent(getOwner().getUsername());
        Element path = new Element("path");
        path.addContent(getPath() + getName());
        
        Element lastModified = new Element("lastModified");        
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        lastModified.addContent(fmt.print(getModified()));

        node.addContent(name);
        node.addContent(perm);
        node.addContent(owner);
        node.addContent(path);
        node.addContent(lastModified);

        
        return node;
    }

    /**
     * Interface method.
     * @throws UnsupportedEncodingException occurs always if called directly with File.
     */
    public String getContent() {
        throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Interface method.
     * @throws UnsupportedEncodingException occurs always if called directly with File.
     */
    public void addFile(File file) {
        throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Interface method.
     * @throws UnsupportedEncodingException occurs always if called directly with File.
     */
    public File getFileByPath (String link){
        throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Removes the file.
     * @throws PermissionDeniedException The user doesn't have the privilege to remove the file.
     */
    public void remove(User user) 
    {
        if(user.getPermissions().canDelete(this))
        {
            setOwner(null);
            setPermissions(null);
            setParent(null);
            deleteDomainObject();
        }
        else
        {
            throw new DeletePermissionException(this.getName(), user.getUsername());
        }
    }

    /**
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of File.
     */
	public String toString(){
		String type = this.getClass().getSimpleName();
	    String permissions = this.getPermissions().toString();	
    
	    String list = type.charAt(0) + " " + permissions + " ";
	    return list;
	}
}
