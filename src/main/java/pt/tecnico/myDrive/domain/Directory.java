
package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.FileNotFoundException;
import pt.tecnico.myDrive.exception.FileIdNotFoundException;
import pt.tecnico.myDrive.exception.NameFileAlreadyExistsException;


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
        super();
        super.init(owner, name,parent, m);
        //setParent(parent);

        addFile(getOwner().createLink(parent,"..", m));
        addFile(getOwner().createLink(this,".", m));
        //addFile(this);
    }

    /**
     * Alternate constructor to create a Link with xml.
     * @param  xml (Element JDOM) represents a File in xml format.
     */
    public Directory(Element xml, User owner) {
        super();
        setOwner(owner);
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
     * Exports a Directory to a persistent state (XML format).
     * @see File
     * @return (Element JDOM) which represents a Directory.
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

    /**
     * Finds a file in a current given path.
     * @param link (String) represents a the path to the file (relative or absolute).
     * @return File represents the file found.
     * @throws FileNotFoundException occurs when the given path is invalid.
     */
    @Override
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
     * Removes the Files in a Directory.
     * @throws PermissionDeniedException The user doesn't have the privilege to remove the directory.
     */
    @Override
    public void remove() {
        for(File f : getFileSet()) {
            f.remove();
        }
        super.remove();       
    }

    /**
     * Returns a list of files.
     * @return String represents the list of files.
     */
    @Override
    public String getContent() {
        return listContent();
    }

    /**
     * @// FIXME: 18/03/16 needs refactoring.
     * @return list (String) returns a list of files inside a directory.
     */
    public String listContent(){
    	String[] names = new String[getFileSet().size()];
    	String[] entries = new String[getFileSet().size()];
        String list = "";
        String tempStr = "";
        int k = 0;
        for(File path : this.getFileSet()){
        	entries[k]= path.toString();
        	names[k] = path.getName();
        	k++;
        }
        for (int t = 0; t < names.length - 1; t++) {
            for (int i= 0; i < names.length - t -1; i++) {
                if(names[i+1].compareTo(names[i])<0) {
                    tempStr = names[i];
                    names[i] = names[i + 1];
                    names[i + 1] = tempStr;
                    tempStr = entries[i];
                    entries[i] = entries[i + 1];
                    entries[i + 1] = tempStr;
                }
            }
        }
        for (int j = 0; j < names.length; j++){
        	list += entries[j] + "\n";        	
        }
        return list;
    }

    /**
     * Adds a File to a Directory.
     * @param file (File) receives a file.
     * @see File
     * @throws NameFileAlreadyExistsException occurs when adding a File in the same Directory has same name.
     */
    @Override
    public void addFile(File file) throws NameFileAlreadyExistsException {
        for (File fName : getFileSet()){
            if (fName.getName().equals(file.getName())){
                throw new NameFileAlreadyExistsException(file.getName());
            }
        }
        super.addFile(file);
    }


    /**
     * Searches for a File by name in a Directory.
     * @param  name (String) receives a String which is the name of the File.
     * @throws FileNotFoundException when there is no File with that name.
     * @return File returns the file with the name received.
     */
    public File searchFile(String name) {
        for(File f: getFileSet())
            if (f.getName().equals(name))
                return f;
        throw new FileNotFoundException(name);
    }

    /**
     * Search a File by id in a Directory
     * @param id (int) receives a String which is the name of the File
     * @see File
     * @throws FileIdNotFoundException
     */
    public File searchFile(int id) throws FileIdNotFoundException {
        for(File f: getFileSet())
            if (f.getId() == id)
                return f;
        throw new FileIdNotFoundException(id);
    }

    /**
     * Overrides original toString() to the current object implementation.
     * @return String represents the output string of a Directory.
     */
    public String toString(){
        String a = super.toString();
        String dim = getFileSet().size() + "";
        String username = this.getOwner().getUsername();
        String modified = getModified().toString("MMM dd hh:mm");

        String rest = dim + " " + username + " " + getId() + " " + modified + " " + this.getName();
        return a + rest;
    }
}
