package pt.tecnico.myDrive.domain;
import org.jdom2.Element;


public class App extends App_Base {
    
    /**
     * Default constructor.
     */
    public App() {
        super();
    }

    /**
     * Alternate construtor to create a App with xml.
     * @param  xml Element (JDOM library type) which represents a File.
     */
    public App(Element xml, User owner) {
        super();
        importXml(xml);
        setOwner(owner);
    }

    /**
     * Alternate construtor to create a App.
     * @param owner User (JDOM library type) which represents a User.
     * @param name String (Java Primitive) which represents the name a App.
     * @param content String (Java Primitive) which represents the content of the App
     */
    public App(User owner, String name, String content, Manager m) {
        super();
        super.init(owner, name, m);
        setContent(content);
    }

    /**
     * Imports a App from persistent state (XML format).
     * @throws ImportDocumentException
     */
    @Override
    public void importXml (Element xml) {
        super.importXml(xml);
    }

    /**
     * Exports an App to a persistent state (XML format),
     * @see PlainFile
     * @return Element (JDOM library type) which represents an App
     */
    @Override
    public Element exportXml () {
        return super.exportXml();
    }
    
    public File getFileByPath (String link){
        throw new UnsupportedOperationException("Not Implemented!");
    }  
}
