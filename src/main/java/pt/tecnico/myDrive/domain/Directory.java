package pt.tecnico.myDrive.domain;
import org.jdom2.Element;


/**
 * 
 */
public class Directory extends Directory_Base {
    
    /**
     * Default Constructor.
     */
    public Directory() {
        super();
    }
    
    /**
     * Imports a Directory to a persistent state (XML format).
     * @throws ImportDocumentException
     */
    @Override
    public void importXml () {
        super.importXml();
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
			filesElement.addContent(f.exportXml());

		return node;
	}

    public File getFileByPath (String link){
    	String[] split = link.split("/",2);
    	String rest = split[1];
    	String nomeInit = split[0];
    	for(File path : this.getFileSet()){
    		if(path.getName().equals(nomeInit)){
    			return path.getFileByPath(rest);
    		}
    	}
    	throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * If the array size equals zero the directory is removed.
     */
    public void remove()
    {
        if(this.getFileSet().size() == 0)
        {
            super.remove();
        }
    }
}
