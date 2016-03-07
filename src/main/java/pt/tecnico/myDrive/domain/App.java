package pt.tecnico.myDrive.domain;
import org.jdom2.Element;


public class App extends App_Base {
    
    public App() {
        super();
    }

    @Override
    public void importXml () {
        super.importXml();
    }

    @Override
    public Element exportXml () {
		return super.exportXml();
    }
	
    public File getFileByPath (String link){
    	throw new UnsupportedOperationException("Not Implemented!");
    }
    
}
