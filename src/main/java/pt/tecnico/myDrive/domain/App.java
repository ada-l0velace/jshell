package pt.tecnico.myDrive.domain;
import org.jdom2.Element;

import java.io.UnsupportedEncodingException;


public class App extends App_Base {
    
    /**
     * Default constructor.
     */
    public App() {
        super();
    }

    /**
     * Alternate constructor to create a App with xml.
     * @param  xml (Element JDOM) which represents a File.
     */
    public App(Element xml, User owner) {
        super();
        importXml(xml);
        setOwner(owner);
    }

    /**
     * Alternate constructor to create a App.
     * @param owner (User) represents a User.
     * @param name (String) represents the name a App.
     * @param content (String) represents the content of the App.
     * @param m (Manager) represents an instance of Manager.
     */
    public App(User owner, String name, String content, Directory parent, Manager m) {
        super();
        super.init(owner, name, parent, m);
        setContent(content);
    }

    /**
     * Imports a App from persistent state (XML format).
     * @see PlainFile
     * @throws ImportDocumentException
     */
    @Override
    public void importXml (Element xml) {
        super.importXml(xml);
    }

    /**
     * Exports an App to a persistent state (XML format),
     * @see PlainFile
     * @return (Element JDOM) which represents an App.
     */
    @Override
    public Element exportXml () {
        return super.exportXml();
    }

    /**
     * Interface method.
     * @throws UnsupportedEncodingException occurs always if called directly with an App.
     */
    public File getFileByPath (String link) {
        throw new UnsupportedOperationException("Not Implemented!");
    }
}
