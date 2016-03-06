package pt.tecnico.myDrive.domain;
import org.jdom2.Element;


public class Directory extends Directory_Base {
    
    public Directory() {
        super();
    }
    
    @Override
    public void importXml () {
        super.importXml();
    }

    @Override
    public Element exportXml () {
        throw new UnsupportedOperationException("Not Implemented!");
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
}
