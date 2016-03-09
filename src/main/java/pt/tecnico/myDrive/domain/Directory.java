package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import pt.tecnico.myDrive.domain.User;
import org.joda.time.DateTime;


public class Directory extends Directory_Base {
    
    /**
     * Default Constructor.
     */
    public Directory() {
        super();
    }

    /**
     * Alternate Constructor for a Directory.
     * @param  owner    User user owner of the file.
     */
    public Directory(User owner) {
        super();
        super.init(owner);
        setOwner(owner);
    }

    /**
     * Alternate construtor to create a Link with xml.
     * @param  xml Element (JDOM library type) which represents a File.
     */
    public Directory(Element xml) {
        super();
        importXml(xml);
    }
    
    /**
     * Imports a Directory to a persistent state (XML format).
     * @throws ImportDocumentException
     */
    @Override
    public void importXml (Element xml) {
        super.importXml(xml);
        Element files = xml.getChild("files");
        for (Element file: files.getChildren("file"))
            addFile(new File(file));
    }

	/**
	 * Exports a Directory to a persistent state (XML format),
	 * @see File
	 * @return Element (JDOM library type) which represents a Directory
	 */
	@Override
	public Element exportXml () {
		Element node = super.exportXml();

		Element filesElement = new Element("files");
		node.addContent(filesElement);
		for (File f: getFileSet())
			node.addContent(f.exportXml());

        return node;
	}

    public File getFileByPath (String link){
    	String[] spliTest = link.split("/");
    	String[] split = spliTest;
    	String rest = "";
    	String nomeInit = link;
    	for(File path : this.getFileSet()){
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
    	throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * If the array size equals zero the directory is removed.
     * @throws PermissionDeniedException The user doesn't have the privilege to remove the directory.
     */
    public void remove()
    {
        for(File f : getFileSet())
        {
            f.remove();
        }
        super.remove();
    }
	
    /**
     * @return list String (Primary java type) with the file names inside of the directory.
     */
    public String listContent(){
    	String list = "";
    	for(File path : this.getFileSet()){
    		list += path.getName() + " ";
    	}
    	return list;
    }
}
