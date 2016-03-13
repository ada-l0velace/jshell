package pt.tecnico.myDrive.domain;


import org.jdom2.Element;
import pt.tecnico.myDrive.domain.User;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.NameFileAlreadyExistsException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
     */
    public Directory(User owner, String name, Link parent, Manager m) {
        super();
        super.init(owner, name, m);
        //setParent(parent);
        addFile(parent);
        addFile(new Link (".", this, parent.getContent() + name + "/" , m));
        //addFile(this);
    }

    /**
     * Alternate construtor to create a Link with xml.
     * @param  xml Element (JDOM library type) which represents a File.
     */
    public Directory(Element xml, User owner) {
        super();
        setOwner(owner);
        importXml(xml);
    }
    
    /**
     * Imports a Directory to a persistent state (XML format).
     * @throws ImportDocumentException
     */
    @Override
    public void importXml (Element xml) {
        super.importXml(xml);
        
        //Element files = xml.getChildren("Files");
        for (Element files : xml.getChildren("Files") ) {
            for (Element link : files.getChildren("Link"))
                addFile(new Link(link, getOwner()));
            for (Element plainFile : files.getChildren("PlainFile"))
                addFile(new PlainFile(plainFile, getOwner()));
            for (Element app : files.getChildren("App"))
                addFile(new App(app, getOwner()));
            for (Element directory : files.getChildren("Directory"))
                addFile(new Directory(directory, getOwner()));
        }
    }

    /**
     * Exports a Directory to a persistent state (XML format),
     * @see File
     * @return Element (JDOM library type) which represents a Directory
     */
    @Override
    public Element exportXml () {
        Element node = super.exportXml();

        Element filesElement = new Element("Files");
        node.addContent(filesElement);
        for (File f: getFileSet())
            filesElement.addContent(f.exportXml());

        return node;
    }

    public File getFileByPath (String link) throws FileNotFoundException {
        String[] spliTest = link.split("/");
        String[] split = spliTest;
        String rest = "";
        String nomeInit = link;
        for(File path : getFileSet()){
            if (spliTest.length != 1){
                split = link.split("/",2);
                rest = split[1];
                nomeInit = split[0];
                if(path.getName().equals(nomeInit)){
                    return path.getFileByPath(rest);
                }
            }
            else{
                if(path.getName().equals(link)){
                    return path;
                }
            }
        }
        throw new FileNotFoundException(nomeInit);
    }

    /**
     * If the array size equals zero the directory is removed.
     * @throws PermissionDeniedException The user doesn't have the privilege to remove the directory.
     */
    public void remove()
    {
        for(File f : getFileSet()) {
            f.remove();
        }
        super.remove();       
    }
    
    public String toString(){
    	String a = super.toString();
    	String dim = getFileSet().size() + "";  	
    	String username = this.getOwner().getUsername();
	    DateFormat d = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    String modified = d.format(this.getModified());
	    
	    String rest = dim + " " + username + " " + this.getId() + " " + modified + " " + this.getName();
	    return a + rest;
    }
    
    /**
     * @return list String (Primary java type) with the file names inside of the directory.
     */
    public String listContent(){
        String list = "";
        for(File path : this.getFileSet()){
        	list += path.toString() + "\n";
        }
        return list;
    }
   
    @Override

    public void addFile(File filetba) throws NameFileAlreadyExistsException {
        for (File fName : getFileSet()){
            if (fName.getName().equals(filetba.getName())){
                throw new NameFileAlreadyExistsException(filetba.getName());
            }
        }
        super.addFile(filetba);
    }


    /**
    * Searches for a File by name in a Directory
    * @param String name recieves a String which is the name of the File
    * @return File returns the file with the name recieved
    * @throws FileNotFoundException when there is no File with that name
    */
    public File searchFile(String name) {
        for(File f: getFileSet())
            if (f.getName().equals(name))
                return f;
        throw new FileNotFoundException(name);
    }

    public File searchFile(int id) {
        for(File f: getFileSet())
            if (f.getId() == id)
                return f;
        return null;
        //throw new FileNotFoundException(id);
    }
    
}
