package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.domain.Permissions;
import pt.tecnico.myDrive.interfaces.IElementXml;

import org.jdom2.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.InvalidNameFileException;

import java.io.UnsupportedEncodingException;
/**
 * 
 */
public class File extends File_Base implements IElementXml {
    
    protected String nameRegex = "[^/\0]*";
    /**
     * Default construtor to create File. 
     */
    protected File() {
        super();
    }

    /**
     * Alternative construtor to create a File.
     * @param owner User user owner of the file.
     * @param name String represents the name of the folder.
     */
    protected File(User owner, String nome, Manager m) {
        super();
        init(owner, nome, m);
    }
    
    /**
     * Alternate construtor to create a File with xml.
     * @param xml Element (JDOM library type) which represents a File.
     */
    protected File(Element xml){
        super();
        importXml(xml);
    }

    /**
     * Protected init Constructor for File
     * @param owner User which represents a File.
     * @param name  String (Primary java type) which represents the name of File.
     */
    protected void init (User owner, String name, Manager m) {
    	int id = m.getLastFileId();
        setId(id+1);
        m.setLastFileId(id+1);
        setName(name);
        setModified(new DateTime(DateTimeZone.UTC));
        setPermissions(new Permissions(owner.getPermissions().getUmask()));
        setOwner(owner);
    }
    
    /**
     * Do override of setName checking for special caracters
     * @param String (JavaPrimitive) which represents a name for the file
     * */
    @Override
    public void setName(String name) throws InvalidNameFileException {
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new InvalidNameFileException(name);
        }
        else{
            super.setName(name);
        }
    }
    
    /**
     * Imports a File from persistent state (XML format).
     * @param xml Element (JDOM library type) which represents a File.
     * @throws ImportDocumentException occurs when there is an error with the import.
     * @see User Permissions  
     */
    @Override
    public void importXml (Element xml) /*ImportDocumentException*/ { 
        Element node = xml;
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

        int id = Integer.parseInt(node.getAttribute("id").getValue());
        String name = new String(node.getAttribute("name").getValue());
        DateTime dateModified = new DateTime(fmt.parseDateTime(node.getAttribute("modified").getValue()));
        
        short umask = Short.parseShort(node.getAttribute("umask").getValue());
        
        String userName = node.getAttribute("owner").getValue();
        User owner = Manager.getInstance().getUserByUsername(userName);

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
        Element node = new Element(this.getClass().getSimpleName());
        node.setAttribute("id", Integer.toString(getId()));
        node.setAttribute("name", getName());
        node.setAttribute("umask", Short.toString(getPermissions().getUmask()));
				node.setAttribute("owner", getOwner().getUsername());
				
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        node.setAttribute("modified", fmt.print(getModified()));
				
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
