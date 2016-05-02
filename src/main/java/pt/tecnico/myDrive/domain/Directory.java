
package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.*;
import pt.tecnico.myDrive.service.dto.DirectoryDto;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.Set;


public class Directory extends Directory_Base {
    
    /**
     * Default Constructor.
     */ 
    public Directory() {
        super();
    }

    /**
     * Alternate Constructor for a Directory.
     * @param  owner User user owner of the file.
     * @param name Represents the name of the folder.
     * @param m represents
     */
    public Directory(User owner, String name, Directory parent, Manager m) {
        this();
        super.init(owner, name, parent, m);
        //getOwner().createLink(parent,"..", this, m);
        //getOwner().createLink(this,".", this, m);
    }

    /**
     * Alternate constructor to create a Link with xml.
     * @param  xml (Element JDOM) represents a File in xml format.
     */
    public Directory(Element xml) {
        super();
        importXml(xml);
    }
    
    /**
     * Imports a Directory to a persistent state (XML format).
     * @see PlainFile Link App Directory
     * @throws ImportDocumentException
     */
    @Override
    public void importXml (Element xml) {
        super.importXml(xml);
    }

    /**
     * @deprecated and replaced with new exportXml
     */
    @Deprecated
    public Element xmlExport(){
        Element node = super.xmlExport();

        Element filesElement = new Element("Files");
        node.addContent(filesElement);

        for (File f: super.getFileSet())
            filesElement.addContent(f.xmlExport());
        
        return node;
    }

    /**
     * Exports a Directory to a persistent state (XML format).
     * @see File
     * @return (Element JDOM) which represents a Directory.
     */
    @Override
    public Element exportXml () {
        
        return super.exportXml();
    }

    /**
     * Finds a file in a current given path.
     * @param link (String) represents a the path to the file (relative or absolute).
     * @return File represents the file found.
     * @throws FileNotFoundException occurs when the given path is invalid.
     */
    @Override
    protected File getFileByPath (String link) throws FileNotFoundException {
    	if(link.equals("")) {
    		return this;
    	}
        String[] spliTest = link.split("/");
        String[] split = spliTest;
        String rest = "";
        String nomeInit = link;
        for(File path : super.getFileSet()) {
            if (spliTest.length != 1) {
                split = link.split("/",2);
                rest = split[1];
                nomeInit = split[0];
                if(path.getName().equals(nomeInit)){
                    return path.getFileByPath(rest);
                }
            }
            else {
                if(path.getName().equals(link)){
                    return path;
                }
            }
        }
        throw new FileNotFoundException(nomeInit);
    }

    /**
     * Removes the Files in a Directory.
     * @throws DeletePermissionException The user doesn't have the privilege to remove the directory.
     */
    @Override
    public void remove(String token) throws DeletePermissionException {
        User user = Manager.getInstance().getSessionByToken(token).getUser();
        if (!user.getPermissions().canWrite(getParent()) || !user.getPermissions().canDelete(this))
            throw new DeletePermissionException(this.getName(), user.getUsername());
        for (File f : super.getFileSet()) {
            if (!user.getPermissions().canDelete(f))
                throw new DeletePermissionException(this.getName(), user.getUsername());
        }
        for (File f : super.getFileSet()) {
                f.remove(token);
        }

        for (Session s: getSessionSet()) {
            s.setCurrentDirectory(s.getUser().getHome());
        }

        super.remove(token);
        deleteDomainObject();
    }

    /**
     * Protects class from other programmers.
     * @throws PublicAcessDeniedException occurs when someone tries to access it publicly.
     */
    @Override
    public Set<File> getFileSet() {
        throw new PublicAcessDeniedException("getFileSet()", "listContent(token)");
    }

    /**
     * @return list (String) returns a list of files inside a directory.
     */
    public Set<File> listContent(String token) throws ReadPermissionException {
    	User user = Manager.getInstance().getUserByToken(token);
    	if (user.getPermissions().canRead(this)) {
    		return super.getFileSet();
    	} 
    	else {
    		throw new ReadPermissionException(this.getName(), user.getName());
    	}
    }

    
    /**
     * Adds a File to a Directory.
     * @param file (File) receives a file.
     * @see File
     * @throws NameFileAlreadyExistsException occurs when adding a File in the same Directory has same name.
     */
    @Override
    public void addFile(File file) throws NameFileAlreadyExistsException {
        for (File fName : super.getFileSet()){
            if (fName.getName().equals(file.getName())){
                throw new NameFileAlreadyExistsException(file.getName());
            }
        }
        super.addFile(file);
    }

    /**
     * Searches for a File by name in a Directory.
     * @param name (String) receives a name of the file.
     * @param token (String) receives the token of the user.
     * @return String returns the name of the file.
     */
    public File searchFile(String name, String token) {
        for(File f: listContent(token))
            if (f.getName().equals(name))
                return f;
        return null;
    }

    /**
     * Unprotected search for file creation.
     * @param name (String) receives a name of the file.
     * @return String returns the name of the file.
     */
    public File searchFile(String name) {
        for(File f: super.getFileSet())
            if (f.getName().equals(name))
                return f;
        return null;
    }

    /**
     * Search a File by id in a Directory
     * @param name (String) receives a String which is the name of the File
     * @see File
     * @throws FileIdNotFoundException
     */
    public File search(String name, String token) throws FileIdNotFoundException {
        for(File f: listContent(token)) {

            if (f.getName().equals(name))
                return f;
        }
        throw new FileNotFoundException(name);
    }

    @Override
    public String getContent(String token) {
        readPermissions (token);
        return (super.getFileSet().size()+2) + "";
    }

    public FileDto getDtoData(String token) {
        return new DirectoryDto(getId(), getName(), getModified(), getPermissions().getUmask(),
                getParent().getName(), getOwner().getName(), toString());
    }

    public FileDto getDotDotData(String token) {
        int isRootDir = 2;
        if (getName().equals("/"))
            isRootDir = 1;
        String op1 = "D" + " " + getParent().getPermissions().toString() + " " +
                (getParent().listContent(token).size()+isRootDir) + " " + getParent().getOwner().getUsername() + " " +
                getParent().getId() + " " + getParent().getModified().toString("MMM dd hh:mm") + " ..";
        return new DirectoryDto(getParent().getId(), "..", getParent().getModified(), getParent().getPermissions().getUmask(),
                getParent().getName(), getParent().getOwner().getName(), op1);
    }

    public FileDto getDotData(String token) {
        int isRootDir = 2;
        if (getName().equals("/"))
            isRootDir = 1;
        String op1 = "D" + " " + getPermissions().toString() + " " +
                (listContent(token).size()+isRootDir) + " " + getOwner().getUsername() + " " +
                getId() + " " + getModified().toString("MMM dd hh:mm") + " .";
        return new DirectoryDto(getId(), ".", getModified(), getPermissions().getUmask(),
                getParent().getName(), getOwner().getName(), op1);
    }

    /**
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of a Directory.
     */
    public String toString(){
        String a = super.toString();
        String dim = (super.getFileSet().size()+2) + "";
        String username = this.getOwner().getUsername();
        String modified = getModified().toString("MMM dd hh:mm");
        String rest = dim + " " + username + " " + getId() + " " + modified + " " + this.getName();
        return a + rest;
    }
}
