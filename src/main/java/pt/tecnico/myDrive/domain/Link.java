package pt.tecnico.myDrive.domain;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


public class Link extends Link_Base {
    
    /**
     * Default Constructor
     */
    public Link() {
        super();
    }

    /**Alternate constructor to create a Link with a path
    * @param name (Java Primitive) which represents the name of the file.
    * @param file (File) which represents a file
    * @param path (Java Primitive) which represents the path of the link.
    * @param m (Manager) which represents the Manager
    */
    public Link(String name, File file, String path, Manager m){
        super();
        setLastId(m);
        setName(name);
        setModified(new DateTime(DateTimeZone.UTC));
        setPermissions(new Permissions(file.getPermissions().getUmask()));    
        setContent(path);
        setOwner(file.getOwner());
    }

    /**
     * Alternate construtor to create a Link with xml.
     * @param  xml Element (JDOM library type) which represents a File.
     */
    public Link(Element xml) {
        super();
        importXml(xml);
    }

    // /**
    //  * Alternate construtor to create a Link.
    //  * @param  name (Java Primitive) which represents the name of the file.
    //  * @param  file (File) which represents a file
    //  */
    // public Link(String name, File file, Manager m) {
    //     super();
    //     setId(1);
    //     setName(name);
    //     setModified(new DateTime(DateTimeZone.UTC));
    //     setPermissions(new Permissions(file.getPermissions().getUmask()));
    //     setContent("-> " + getFilePath());
        // setOwner(file.getOwner());
    // }
    
    /**
     * [getFilePath description]
     * @return [description]
     */
    public String getFilePath() {
        return "";
    }

    /**
     * Imports a Link from persistent state (XML format).
     * @throws ImportDocumentException
     */
    @Override
    public void importXml (Element xml) {
        super.importXml(xml);
    }

    /**
     * Exports a Link to a persistent state (XML format),
     * @see PlainFile
     * @return Element (JDOM library type) which represents a Link
     */
    @Override
    public Element exportXml () {
        return super.exportXml();
    }

    public File getFileByPath (String link){
        throw new UnsupportedOperationException("Not Implemented!");
    }
}
